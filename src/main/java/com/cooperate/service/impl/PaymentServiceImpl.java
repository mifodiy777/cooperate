package com.cooperate.service.impl;

import com.cooperate.dao.PaymentDAO;
import com.cooperate.entity.Contribution;
import com.cooperate.entity.Garag;
import com.cooperate.entity.Payment;
import com.cooperate.service.ContributionService;
import com.cooperate.service.GaragService;
import com.cooperate.service.PaymentService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentDAO paymentDAO;

    @Autowired
    private ContributionService contributionService;

    @Autowired
    private GaragService garagService;

    @Override
    public List<Integer> findYears() {
        return paymentDAO.findYears();
    }

    @Override
    @Transactional
    public Payment saveOrUpdate(Payment payment) {
        return paymentDAO.save(payment);
    }


    @Override
    public List<Payment> findByYear(Integer year) {
        return paymentDAO.findByYear(year);
    }

    @Override
    public Payment getPayment(Integer id) {
        return paymentDAO.getOne(id);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        paymentDAO.delete(id);
    }

    //Возвращает платеж для определенного гаража с остатками денег
    @Override
    public List<Payment> getPaymentOnGarag(Garag garag) {
        return paymentDAO.getPaymentOnGarag(garag.getId());
    }

    @Override
    public Integer getMaxNumber() {
        Integer number = paymentDAO.getMaxValueNumber();
        number = (number == null) ? 1 : paymentDAO.getMaxValueNumber() + 1;
        return number;
    }

    //Метод платежа
    @Override
    @Transactional
    public Payment pay(Payment payment, Boolean isCreateNewPeriod, String type) {
        //Получаем гараж
        Garag garag = garagService.getGarag(payment.getGarag().getId());
        Calendar now = Calendar.getInstance();
        if (!isCreateNewPeriod) {
            //Назначили время
            if (payment.getDatePayment() == null) {
                payment.setDatePayment(now);
                payment.setYear(now.get(Calendar.YEAR));
            } else {
                payment.setYear(payment.getDatePayment().get(Calendar.YEAR));
            }
            //Назначили номер
            payment.setNumber(getMaxNumber());
            payment.setGarag(garag);
            payment.setFio(garag.getPerson().getFIO());
        }
        if (type.equals("adding")) {
            payment.setAdditionallyPay(payment.getPay());
            payment.setPay(0f);
            payment.setDebtPastPay(garagService.sumContribution(payment.getGarag()));
            return paymentDAO.save(payment);
        }
        int size = garag.getContributions().size();
        int i = 1;
        float oldContribute = garag.getOldContribute();
        if (oldContribute != 0) {
            if (payment.getPay() <= oldContribute) {
                payment.setOldContributePay(payment.getPay());
                garag.setOldContribute(oldContribute - payment.getPay());
                payment.setPay(0);
            } else {
                payment.setOldContributePay(oldContribute);
                payment.setPay(payment.getPay() - oldContribute);
                garag.setOldContribute(0f);
            }
        }
        for (Contribution c : garag.getContributions()) {
            if (payment.getPay() == 0) {
                break;
            }
            if (c.getSumFixed() + c.getFines() == 0) {
                continue;
            }
            if (payment.getPay() >= c.getSumFixed()) {
                Float reminder = c.getSumFixed();
                payment.setContributePay(payment.getContributePay() + c.getContribute());
                c.setContribute(0f);
                payment.setContLandPay(payment.getContLandPay() + c.getContLand());
                c.setContLand(0f);
                payment.setContTargetPay(payment.getContTargetPay() + c.getContTarget());
                c.setContTarget(0f);
                c.setFinesOn(false);
                payment.setPay(payment.getPay() - reminder);
                //Если взнос уплачен проверяем оплату по пеням.
                if (payment.getPay() != 0) {
                    if (c.getFines() >= 0 && payment.getPay() >= c.getFines()) {
                        payment.setFinesPay(payment.getFinesPay() + c.getFines());
                        payment.setPay(payment.getPay() - c.getFines());
                        c.setFines(0);
                    } else {
                        payment.setFinesPay(payment.getFinesPay() + Math.round(payment.getPay()));
                        c.setFines(c.getFines() - Math.round(payment.getPay()));
                        payment.setPay(0);
                    }
                }
            } else {
                if (c.getContribute() > payment.getPay()) {
                    payment.setContributePay(payment.getContributePay() + payment.getPay());
                    c.setContribute(c.getContribute() - payment.getPay());
                    payment.setPay(0);
                } else {
                    payment.setPay(payment.getPay() - c.getContribute());
                    payment.setContributePay(payment.getContributePay() + c.getContribute());
                    c.setContribute(0f);
                }
                if (c.getContLand() > payment.getPay()) {
                    payment.setContLandPay(payment.getContLandPay() + payment.getPay());
                    c.setContLand(c.getContLand() - payment.getPay());
                    payment.setPay(0);
                } else {
                    payment.setPay(payment.getPay() - c.getContLand());
                    payment.setContLandPay(payment.getContLandPay() + c.getContLand());
                    c.setContLand(0f);
                }
                if (c.getContTarget() > payment.getPay()) {
                    payment.setContTargetPay(payment.getContTargetPay() + payment.getPay());
                    c.setContTarget(c.getContTarget() - payment.getPay());
                    payment.setPay(0);
                }
                //Для текущего года при частичной оплате в любом месяце пени выключаются
                if(c.getYear().equals(now.get(Calendar.YEAR))){
                    c.setFinesOn(false);
                }
            }
            contributionService.saveOrUpdate(c);
            i++;
        }
        payment.setDebtPastPay(garagService.sumContribution(payment.getGarag()));
        return paymentDAO.save(payment);
    }

    @Override
    public HSSFWorkbook reportPayments(Calendar start, Calendar end) {
        HSSFWorkbook workBook = new HSSFWorkbook();
        HSSFSheet sheet = workBook.createSheet("Список платежей");
        sheet.setActive(true);
        HSSFRow row = sheet.createRow(0);
        String[] hatCells = new String[]{"№", "Счет", "Дата", "Гараж", "ФИО", "Сумма", "Членский взнос",
                "Аренда земли", "Целевой взнос", "Пени", "Доп.взнос", "Долги прошлых лет", "Остаток"};
        CellStyle headerStyle = workBook.createCellStyle();
        headerStyle.setWrapText(true);
        headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headerStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
        Font font = workBook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerStyle.setFont(font);
        for (int i = 0; i < hatCells.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(hatCells[i]);
            cell.setCellStyle(headerStyle);
            if (i == 0) {
                sheet.setColumnWidth(i, (short) (5 * 256));
            }
            if (i == 1) {
                sheet.setColumnWidth(i, (short) (10 * 256));
            }
            if (i == 2) {
                sheet.setColumnWidth(i, (short) (12 * 256));
            }
            if (i == 3) {
                sheet.setColumnWidth(i, (short) (10 * 256));
            }
            if (i == 4) {
                sheet.setColumnWidth(i, (short) (40 * 256));
            }
            if (i >= 5) {
                sheet.setColumnWidth(i, (short) (20 * 256));
            }
        }
        int numberRow = 1;
        //При выборе например 18 числа будет выборка всех платеже по 19.01.01 00:00:00
        end.set(Calendar.DAY_OF_MONTH, end.get(Calendar.DAY_OF_MONTH) + 1);
        for (Payment p : paymentDAO.findByDateBetween(start, end)) {
            HSSFRow nextRow = sheet.createRow(numberRow);
            HSSFCell countCell = nextRow.createCell(0);
            countCell.setCellValue(numberRow);
            HSSFCell numberCell = nextRow.createCell(1);
            numberCell.setCellValue(p.getNumber());
            Date dt = p.getDatePayment().getTime();
            DateFormat ndf = new SimpleDateFormat("dd/MM/yyyy");
            String dateFull = ndf.format(dt);
            HSSFCell datePay = nextRow.createCell(2);
            datePay.setCellValue(dateFull);
            HSSFCell garagCell = nextRow.createCell(3);
            garagCell.setCellValue(p.getGarag().getName());
            HSSFCell fioCell = nextRow.createCell(4);
            fioCell.setCellValue(p.getFio());
            Float sum = p.getContributePay() + p.getContLandPay() + p.getContTargetPay() + p.getFinesPay() +
                    p.getPay() + p.getAdditionallyPay() + p.getOldContributePay();
            HSSFCell sumPayColumn = nextRow.createCell(5);
            sumPayColumn.setCellValue(sum);
            HSSFCell contributeColumn = nextRow.createCell(6);
            contributeColumn.setCellValue(p.getContributePay());
            HSSFCell landColumn = nextRow.createCell(7);
            landColumn.setCellValue(p.getContLandPay());
            HSSFCell tagetColumn = nextRow.createCell(8);
            tagetColumn.setCellValue(p.getContTargetPay());
            HSSFCell finesColumn = nextRow.createCell(9);
            finesColumn.setCellValue(p.getFinesPay());
            HSSFCell addingColumn = nextRow.createCell(10);
            addingColumn.setCellValue(p.getAdditionallyPay());
            HSSFCell oldContributeColumn = nextRow.createCell(11);
            oldContributeColumn.setCellValue(p.getOldContributePay());
            HSSFCell reminderColumn = nextRow.createCell(12);
            reminderColumn.setCellValue(p.getPay());
            numberRow++;
        }

        if (numberRow != 1) {
            CellStyle footerStyle = workBook.createCellStyle();
            footerStyle.setWrapText(true);
            footerStyle.setAlignment(CellStyle.ALIGN_CENTER);
            footerStyle.setBorderTop(CellStyle.BORDER_MEDIUM);
            Font fontFooter = workBook.createFont();
            fontFooter.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            footerStyle.setFont(fontFooter);
            HSSFRow resumeRow = sheet.createRow(numberRow);
            HSSFCell resume = resumeRow.createCell(4);
            resume.setCellValue("Итого:");
            resume.setCellStyle(footerStyle);
            HSSFCell allContributeResume = resumeRow.createCell(5);
            allContributeResume.setCellType(HSSFCell.CELL_TYPE_FORMULA);
            allContributeResume.setCellFormula("SUM(F2:F" + numberRow + ")");
            allContributeResume.setCellStyle(footerStyle);
            HSSFCell contributeResume = resumeRow.createCell(6);
            contributeResume.setCellType(HSSFCell.CELL_TYPE_FORMULA);
            contributeResume.setCellFormula("SUM(G2:G" + numberRow + ")");
            contributeResume.setCellStyle(footerStyle);
            HSSFCell landResume = resumeRow.createCell(7);
            landResume.setCellType(HSSFCell.CELL_TYPE_FORMULA);
            landResume.setCellFormula("SUM(H2:H" + numberRow + ")");
            landResume.setCellStyle(footerStyle);
            HSSFCell tagetResume = resumeRow.createCell(8);
            tagetResume.setCellType(HSSFCell.CELL_TYPE_FORMULA);
            tagetResume.setCellFormula("SUM(I2:I" + numberRow + ")");
            tagetResume.setCellStyle(footerStyle);
            HSSFCell finesResume = resumeRow.createCell(9);
            finesResume.setCellType(HSSFCell.CELL_TYPE_FORMULA);
            finesResume.setCellFormula("SUM(J2:J" + numberRow + ")");
            finesResume.setCellStyle(footerStyle);
            HSSFCell addingResume = resumeRow.createCell(10);
            addingResume.setCellType(HSSFCell.CELL_TYPE_FORMULA);
            addingResume.setCellFormula("SUM(K2:K" + numberRow + ")");
            addingResume.setCellStyle(footerStyle);
            HSSFCell oldResume = resumeRow.createCell(11);
            oldResume.setCellType(HSSFCell.CELL_TYPE_FORMULA);
            oldResume.setCellFormula("SUM(L2:L" + numberRow + ")");
            oldResume.setCellStyle(footerStyle);
            HSSFCell reminderResume = resumeRow.createCell(12);
            reminderResume.setCellType(HSSFCell.CELL_TYPE_FORMULA);
            reminderResume.setCellFormula("SUM(M2:M" + numberRow + ")");
            reminderResume.setCellStyle(footerStyle);
        }
        return workBook;
    }
}

