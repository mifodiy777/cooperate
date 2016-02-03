package com.cooperate.service.impl;

import com.cooperate.dao.PaymentDAO;
import com.cooperate.entity.Address;
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
    PaymentDAO paymentDAO;

    @Autowired
    ContributionService contributionService;

    @Autowired
    GaragService garagService;

    @Override
    public List<Integer> findYears() {
        return paymentDAO.findYears();
    }

    @Override
    @Transactional
    public Payment saveOrUpdate(Payment rent) {
        return paymentDAO.save(rent);
    }

    @Override
    @Transactional
    public List<Payment> getPayments() {
        return paymentDAO.findAll();
    }

    @Override
    public List<Payment> findByYear(Integer year) {
        return paymentDAO.findByYear(year);
    }

    @Override
    @Transactional
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
    public Payment getPaymentOnGarag(Garag garag) {
        return paymentDAO.getPaymentOnGarag(garag.getId());
    }

    @Override
    public Integer getMaxNumber() {
        return paymentDAO.getMaxValueNumber();
    }

    //Метод платежа
    @Override
    @Transactional
    public void pay(Payment payment) {
        Garag garag = garagService.getGarag(payment.getGarag().getId());
        Calendar now = Calendar.getInstance();
        String fio = garag.getPerson().getLastName() + " " + garag.getPerson().getName() +
                " " + garag.getPerson().getFatherName();
        payment.setFio(fio);
        //Находим длинну коллекции
        int size = garag.getContributions().size();
        //Инициализируем счетчик
        int i = 0;
        //Достаем все начисления за все года отсортированные по году ASC
        for (Contribution c : garag.getContributions()) {
            i++;
            // Есть ли долг по членскому взносу
            if (c.getContribute() != 0f) {
                //Если он меньше платежа идем дальше
                if (c.getContribute() < payment.getPay()) {
                    //Вносим значения суммы за членский взнос в платеж(чек)
                    payment.setContributePay(payment.getContributePay() + c.getContribute());
                    // Вычитаем потраченные деньги из платежа
                    payment.setPay(payment.getPay() - c.getContribute());
                    //Обнуляем долг членского взноса за текущий год
                    c.setContribute(0f);
                    //Проверяем  долг за землю меньше оставшихся средств платежа
                    if (c.getContLand() < payment.getPay()) {
                        //Вносим значения суммы за аренду в платеж(чек)
                        payment.setContLandPay(payment.getContLandPay() + c.getContLand());
                        //Вычитаем
                        payment.setPay(payment.getPay() - c.getContLand());
                        //Обнуляем
                        c.setContLand(0f);
                        if (c.getContTarget() < payment.getPay()) {
                            payment.setContTargetPay(payment.getContTargetPay() + c.getContTarget());
                            payment.setPay(payment.getPay() - c.getContTarget());
                            c.setContTarget(0f);
                            c.setFinesOn(false);
                            if (c.getFines() < payment.getPay()) {
                                payment.setFinesPay(payment.getFinesPay() + c.getFines());
                                payment.setPay(payment.getPay() - c.getFines());
                                c.setFines(0);
                                if (size == i) {
                                    c.setBalance(payment.getPay());
                                    paymentDAO.save(payment);
                                    contributionService.saveOrUpdate(c);
                                }
                            }
                            if (c.getFines() == payment.getPay()) {
                                payment.setFinesPay(payment.getFinesPay() + c.getFines());
                                payment.setPay(payment.getPay() - c.getFines());
                                c.setFines(0);
                                paymentDAO.save(payment);
                                contributionService.saveOrUpdate(c);
                                break;
                            }
                            if (c.getFines() > payment.getPay()) {
                                payment.setFinesPay(payment.getFinesPay() + (int) payment.getPay());
                                c.setFines(c.getFines() - (int) payment.getPay());
                                payment.setPay(0f);
                                paymentDAO.save(payment);
                                contributionService.saveOrUpdate(c);
                                break;
                            }
                        }
                        if (c.getContTarget() == payment.getPay()) {
                            payment.setContTargetPay(payment.getContTargetPay() + c.getContTarget());
                            c.setContTarget(c.getContTarget() - payment.getPay());
                            payment.setPay(0f);
                            c.setFinesOn(false);
                            paymentDAO.save(payment);
                            contributionService.saveOrUpdate(c);
                            break;
                        }
                        if (c.getContTarget() > payment.getPay()) {
                            payment.setContTargetPay(payment.getContTargetPay() + payment.getPay());
                            c.setContTarget(c.getContTarget() - payment.getPay());
                            payment.setPay(0f);
                            paymentDAO.save(payment);
                            contributionService.saveOrUpdate(c);
                            break;
                        }
                    }
                    if (c.getContLand() == payment.getPay()) {
                        payment.setContLandPay(payment.getContLandPay() + c.getContLand());
                        c.setContLand(c.getContLand() - payment.getPay());
                        paymentDAO.save(payment);
                        contributionService.saveOrUpdate(c);
                        break;
                    }
                    if (c.getContLand() > payment.getPay()) {
                        payment.setContLandPay(payment.getContLandPay() + payment.getPay());
                        c.setContLand(c.getContLand() - payment.getPay());
                        payment.setPay(0f);
                        paymentDAO.save(payment);
                        contributionService.saveOrUpdate(c);
                        break;
                    }

                }
                if (c.getContribute() == payment.getPay()) {
                    payment.setContributePay(payment.getContributePay() + c.getContribute());
                    c.setContribute(c.getContribute() - payment.getPay());
                    paymentDAO.save(payment);
                    contributionService.saveOrUpdate(c);
                    break;
                }
                if (c.getContribute() > payment.getPay()) {
                    payment.setContributePay(payment.getContributePay() + payment.getPay());
                    c.setContribute(c.getContribute() - payment.getPay());
                    payment.setPay(0f);
                    paymentDAO.save(payment);
                    contributionService.saveOrUpdate(c);
                    break;
                }
            }
            if (c.getContLand() != 0) {
                if (c.getContLand() < payment.getPay()) {
                    //Вносим значения суммы за аренду в платеж(чек)
                    payment.setContLandPay(payment.getContLandPay() + c.getContLand());
                    //Вычитаем
                    payment.setPay(payment.getPay() - c.getContLand());
                    //Обнуляем
                    c.setContLand(0f);
                    if (c.getContTarget() < payment.getPay()) {
                        payment.setContTargetPay(payment.getContTargetPay() + c.getContTarget());
                        payment.setPay(payment.getPay() - c.getContTarget());
                        c.setContTarget(0f);
                        c.setFinesOn(false);
                        if (c.getFines() < payment.getPay()) {
                            payment.setFinesPay(payment.getFinesPay() + c.getFines());
                            payment.setPay(payment.getPay() - c.getFines());
                            c.setFines(0);
                            if (size == i) {
                                c.setBalance(payment.getPay());
                                paymentDAO.save(payment);
                                contributionService.saveOrUpdate(c);
                            }

                        }
                        if (c.getFines() == payment.getPay()) {
                            payment.setFinesPay(payment.getFinesPay() + c.getFines());
                            payment.setPay(payment.getPay() - c.getFines());
                            c.setFines(0);
                            paymentDAO.save(payment);
                            contributionService.saveOrUpdate(c);
                            break;
                        }
                        if (c.getFines() > payment.getPay()) {
                            payment.setFinesPay(payment.getFinesPay() + (int) payment.getPay());
                            c.setFines(c.getFines() - (int) payment.getPay());
                            payment.setPay(0f);
                            paymentDAO.save(payment);
                            contributionService.saveOrUpdate(c);
                            break;
                        }
                    }
                    if (c.getContTarget() == payment.getPay()) {
                        payment.setContTargetPay(payment.getContTargetPay() + c.getContTarget());
                        c.setContTarget(c.getContTarget() - payment.getPay());
                        payment.setPay(0f);
                        c.setFinesOn(false);
                        paymentDAO.save(payment);
                        contributionService.saveOrUpdate(c);
                        break;
                    }
                    if (c.getContTarget() > payment.getPay()) {
                        payment.setContTargetPay(payment.getContTargetPay() + payment.getPay());
                        c.setContTarget(c.getContTarget() - payment.getPay());
                        payment.setPay(0f);
                        paymentDAO.save(payment);
                        contributionService.saveOrUpdate(c);
                        break;
                    }
                }
                if (c.getContLand() == payment.getPay()) {
                    payment.setContLandPay(payment.getContLandPay() + c.getContLand());
                    c.setContLand(c.getContLand() - payment.getPay());
                    paymentDAO.save(payment);
                    contributionService.saveOrUpdate(c);
                    break;
                }
                if (c.getContLand() > payment.getPay()) {
                    payment.setContLandPay(payment.getContLandPay() + payment.getPay());
                    c.setContLand(c.getContLand() - payment.getPay());
                    payment.setPay(0f);
                    paymentDAO.save(payment);
                    contributionService.saveOrUpdate(c);
                    break;
                }
            }
            if (c.getContTarget() != 0) {
                if (c.getContTarget() < payment.getPay()) {
                    payment.setContTargetPay(payment.getContTargetPay() + c.getContTarget());
                    payment.setPay(payment.getPay() - c.getContTarget());
                    c.setContTarget(0f);
                    c.setFinesOn(false);
                    if (c.getFines() < payment.getPay()) {
                        payment.setFinesPay(payment.getFinesPay() + c.getFines());
                        payment.setPay(payment.getPay() - c.getFines());
                        c.setFines(0);
                        if (size == i) {
                            c.setBalance(payment.getPay());
                            paymentDAO.save(payment);
                            contributionService.saveOrUpdate(c);
                        }

                    }
                    if (c.getFines() == payment.getPay()) {
                        payment.setFinesPay(payment.getFinesPay() + c.getFines());
                        payment.setPay(payment.getPay() - c.getFines());
                        c.setFines(0);
                        paymentDAO.save(payment);
                        contributionService.saveOrUpdate(c);
                        break;
                    }
                    if (c.getFines() > payment.getPay()) {
                        payment.setFinesPay(payment.getFinesPay() + (int) payment.getPay());
                        c.setFines(c.getFines() - (int) payment.getPay());
                        payment.setPay(0f);
                        paymentDAO.save(payment);
                        contributionService.saveOrUpdate(c);
                        break;
                    }
                }
                if (c.getContTarget() == payment.getPay()) {
                    payment.setContTargetPay(payment.getContTargetPay() + c.getContTarget());
                    c.setContTarget(c.getContTarget() - payment.getPay());
                    payment.setPay(0f);
                    c.setFinesOn(false);
                    paymentDAO.save(payment);
                    contributionService.saveOrUpdate(c);
                    break;
                }
                if (c.getContTarget() > payment.getPay()) {
                    payment.setContTargetPay(payment.getContTargetPay() + payment.getPay());
                    c.setContTarget(c.getContTarget() - payment.getPay());
                    payment.setPay(0f);
                    paymentDAO.save(payment);
                    contributionService.saveOrUpdate(c);
                    break;
                }

            }
            if (c.getFines() != 0) {
                if (c.getFines() < payment.getPay()) {
                    payment.setFinesPay(payment.getFinesPay() + c.getFines());
                    payment.setPay(payment.getPay() - c.getFines());
                    c.setFines(0);
                    if (size == i) {
                        c.setBalance(payment.getPay());
                        paymentDAO.save(payment);
                        contributionService.saveOrUpdate(c);
                    }

                }
                if (c.getFines() == payment.getPay()) {
                    payment.setFinesPay(payment.getFinesPay() + c.getFines());
                    payment.setPay(payment.getPay() - c.getFines());
                    c.setFines(0);
                    paymentDAO.save(payment);
                    contributionService.saveOrUpdate(c);
                    break;
                }
                if (c.getFines() > payment.getPay()) {
                    payment.setFinesPay(payment.getFinesPay() + (int) payment.getPay());
                    c.setFines(c.getFines() - (int) payment.getPay());
                    payment.setPay(0f);
                    paymentDAO.save(payment);
                    contributionService.saveOrUpdate(c);
                    break;
                }
            }
            if (size == i) {
                c.setBalance(payment.getPay());
                paymentDAO.save(payment);
                contributionService.saveOrUpdate(c);
            }
        }
    }

    @Override
    public HSSFWorkbook reportPayments(Integer year) {
        HSSFWorkbook workBook = new HSSFWorkbook();
        HSSFSheet sheet = workBook.createSheet("Список платежей " + year + " г");
        sheet.setActive(true);
        HSSFRow row = sheet.createRow(0);
        String[] hatCells = new String[]{"№", "Счет", "Дата", "Гараж", "ФИО", "Сумма", "Членский взнос",
                "Аренда земли", "Целевой взнос", "Пени", "Доп.взнос", "Остаток"};
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
                sheet.setColumnWidth(i, (short) (50 * 256));
            }
            if (i >= 5) {
                sheet.setColumnWidth(i, (short) (15 * 256));
            }
        }
        int numberRow = 1;
        for (Payment p : paymentDAO.findByYear(year)) {
            HSSFRow nextRow = sheet.createRow(numberRow);
            HSSFCell countCell = nextRow.createCell(0);
            countCell.setCellValue(numberRow);
            HSSFCell numberCell = nextRow.createCell(1);
            numberCell.setCellValue(p.getNumber());
            Date dt = p.getDatePayment().getTime();
            DateFormat ndf = new SimpleDateFormat("dd/MM/yyyy");
            String dateFull = ndf.format(dt);
            HSSFCell datePay = row.createCell(2);
            datePay.setCellValue(dateFull);
            HSSFCell garagCell = row.createCell(3);
            garagCell.setCellValue(p.getGarag().getSeries() + "-" + p.getGarag().getNumber());
            HSSFCell fioCell = nextRow.createCell(4);
            fioCell.setCellValue(p.getFio());
            Float sum = p.getContributePay() + p.getContLandPay() + p.getContTargetPay() + p.getFinesPay() +
                    p.getPay() + p.getAdditionallyPay();
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
            HSSFCell reminderColumn = nextRow.createCell(11);
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
        }
        return workBook;
    }
}

