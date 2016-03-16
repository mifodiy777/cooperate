package com.cooperate.service.impl;

import com.cooperate.comparator.GaragComparator;
import com.cooperate.dao.GaragDAO;
import com.cooperate.entity.*;
import com.cooperate.service.GaragService;
import com.cooperate.service.RentService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by KuzminKA on 17.06.2015.
 */
@Service
public class GaragServiceImpl implements GaragService {

    @Autowired
    private GaragDAO garagDAO;

    @Autowired
    private RentService rentService;

    @Transactional
    public Garag saveOrUpdate(Garag garag) {
        if (garag.getId() == null && garag.getPerson() != null) {
            if (garag.getPerson().getId() != null) {
                Person p = garag.getPerson();
                garag.setPerson(null);
                Garag getGarag = garagDAO.save(garag);
                getGarag.setPerson(p);
                return garagDAO.save(getGarag);
            }
        }
        return garagDAO.save(garag);
    }

    @Transactional
    public void delete(Integer id) {
        garagDAO.delete(id);
    }

    public List<Garag> getGarags() {
        return garagDAO.findAll();
    }

    @Override
    public List<String> getSeries() {
        return garagDAO.getSeries();
    }

    @Override
    public List<Garag> findBySeries(String series) {
        return garagDAO.findBySeries(series);
    }

    @Override
    public List<Garag> getGaragForPersonBenefits() {
        return garagDAO.getGaragForPersonBenefits();
    }

    @Override
    public Garag getGarag(Integer id) {
        return garagDAO.findOne(id);
    }

    @Override
    //Общая сумма долга
    public Float sumContribution(Garag garag) {
        Float sum = garag.getOldContribute();
        for (Contribution c : garagDAO.getOne(garag.getId()).getContributions()) {
            sum += c.getSumFixed() + c.getFines();
        }
        return sum;
    }

    @Override
    //Существует ли гараж
    public Boolean existGarag(Garag garag) {
        if (garag.getId() == null) {
            return garagDAO.countBySeriesAndNumber(garag.getSeries(), garag.getNumber()) == 0;
        } else {
            return garagDAO.countBySeriesAndNumberAndIdNot(garag.getSeries(), garag.getNumber(), garag.getId()) == 0;
        }
    }


    @Override
    public HSSFWorkbook reportAll() {
        HSSFWorkbook workBook = new HSSFWorkbook();
        HSSFSheet sheet = workBook.createSheet("Список членов ГК");
        sheet.setActive(true);
        HSSFRow row = sheet.createRow(0);
        String[] hatCells = new String[]{"№", "Гараж", "ФИО", "Телефон", "Адрес", "Дополнительная информация", "Лготы"};
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
                sheet.setColumnWidth(i, (short) (40 * 256));
            }
            if (i == 3) {
                sheet.setColumnWidth(i, (short) (15 * 256));
            }
            if (i == 4) {
                sheet.setColumnWidth(i, (short) (50 * 256));
            }
            if (i == 5) {
                sheet.setColumnWidth(i, (short) (20 * 256));
            }
            if (i == 6) {
                sheet.setColumnWidth(i, (short) (30 * 256));
            }
        }
        int numberRow = 1;
        List<Garag> garagList = getGarags();
        Collections.sort(garagList, new GaragComparator());
        for (Garag garag : garagList) {
            HSSFRow nextRow = sheet.createRow(numberRow);
            HSSFCell countCell = nextRow.createCell(0);
            countCell.setCellValue(numberRow);
            HSSFCell garagCell = nextRow.createCell(1);
            garagCell.setCellValue(garag.getName());
            if (garag.getPerson() != null) {
                HSSFCell fioCell = nextRow.createCell(2);
                fioCell.setCellValue(garag.getPerson().getFIO());
                HSSFCell phoneCell = nextRow.createCell(3);
                phoneCell.setCellValue(garag.getPerson().getTelephone());
                HSSFCell addressCell = nextRow.createCell(4);
                addressCell.setCellValue(garag.getPerson().getAddress().getAddr());
                HSSFCell additionlaCell = nextRow.createCell(5);
                additionlaCell.setCellValue(garag.getPerson().getAdditionalInformation());
                HSSFCell benefitsCell = nextRow.createCell(6);
                benefitsCell.setCellValue(garag.getPerson().getBenefits());
            }
            numberRow++;
        }
        return workBook;
    }

    @Override
    public HSSFWorkbook reportBenefitsPerson() {
        HSSFWorkbook workBook = new HSSFWorkbook();
        HSSFSheet sheet = workBook.createSheet("Список льготников ГК");
        sheet.setActive(true);
        HSSFRow row = sheet.createRow(0);
        String[] hatCells = new String[]{"№", "ФИО", "Гараж", "Лготы", "Площадь"};
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
                sheet.setColumnWidth(i, (short) (40 * 256));
            }
            if (i == 2) {
                sheet.setColumnWidth(i, (short) (40 * 256));
            }
            if (i == 3) {
                sheet.setColumnWidth(i, (short) (60 * 256));
            }
            if (i == 4) {
                sheet.setColumnWidth(i, (short) (20 * 256));
            }
        }
        int numberRow = 1;
        List<Garag> garagList = getGaragForPersonBenefits();
        Collections.sort(garagList, new GaragComparator());
        for (Garag garag : garagList) {
            HSSFRow nextRow = sheet.createRow(numberRow);
            HSSFCell countCell = nextRow.createCell(0);
            countCell.setCellValue(numberRow);
            HSSFCell fioCell = nextRow.createCell(1);
            fioCell.setCellValue(garag.getPerson().getFIO());
            HSSFCell garagCell = nextRow.createCell(2);
            garagCell.setCellValue("Членская книжка ГК №23 " + garag.getSeries() + " ряд-" + garag.getNumber() + " место");
            HSSFCell benefitsCell = nextRow.createCell(3);
            benefitsCell.setCellValue(garag.getPerson().getBenefits());
            HSSFCell squedCell = nextRow.createCell(4);
            Long count = garagDAO.count();
            squedCell.setCellValue((51004 / count) + " кв. м.");
            numberRow++;
        }
        return workBook;
    }

    @Override
    public HSSFWorkbook reportContribute() {
        HSSFWorkbook workBook = new HSSFWorkbook();
        HSSFSheet sheet = workBook.createSheet("Список должников ГК");
        sheet.setActive(true);
        HSSFRow row = sheet.createRow(0);
        String[] hatCells = new String[]{"№", "Гараж", "ФИО", "Телефон", "Адрес",
                "Общий долг", "Членские взносы", "За землю", "Целевой взнос", "Пени", "Долги прошлых лет"};
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
                sheet.setColumnWidth(i, (short) (40 * 256));
            }
            if (i == 3) {
                sheet.setColumnWidth(i, (short) (15 * 256));
            }
            if (i == 4) {
                sheet.setColumnWidth(i, (short) (40 * 256));
            }
            if (i >= 5) {
                sheet.setColumnWidth(i, (short) (20 * 256));
            }
        }
        int numberRow = 1;
        List<Garag> garagList = garagDAO.getGaragDebt();
        Collections.sort(garagList, new GaragComparator());
        for (Garag garag : garagList) {
            HSSFRow nextRow = sheet.createRow(numberRow);
            HSSFCell countCell = nextRow.createCell(0);
            countCell.setCellValue(numberRow);
            HSSFCell garagCell = nextRow.createCell(1);
            garagCell.setCellValue(garag.getName());
            HSSFCell fioCell = nextRow.createCell(2);
            fioCell.setCellValue(garag.getPerson().getFIO());
            HSSFCell phoneCell = nextRow.createCell(3);
            phoneCell.setCellValue(garag.getPerson().getTelephone());
            HSSFCell addressCell = nextRow.createCell(4);
            addressCell.setCellValue(garag.getPerson().getAddress().getAddr());
            Float cont = 0f;
            Float land = 0f;
            Float target = 0f;
            Integer fines = 0;
            for (Contribution c : garag.getContributions()) {
                cont += c.getContribute();
                land += c.getContLand();
                target += c.getContTarget();
                fines += c.getFines();
            }
            Float sum = cont + land + target + fines;
            HSSFCell allContributeColumn = nextRow.createCell(5);
            allContributeColumn.setCellValue(sum);
            HSSFCell contributeColumn = nextRow.createCell(6);
            contributeColumn.setCellValue(cont);
            HSSFCell landColumn = nextRow.createCell(7);
            landColumn.setCellValue(land);
            HSSFCell tagetColumn = nextRow.createCell(8);
            tagetColumn.setCellValue(target);
            HSSFCell finesColumn = nextRow.createCell(9);
            finesColumn.setCellValue(fines);
            HSSFCell oldColumn = nextRow.createCell(10);
            oldColumn.setCellValue(garag.getOldContribute());
            numberRow++;
        }
        if (!garagList.isEmpty()) {
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
            HSSFCell oldResume = resumeRow.createCell(10);
            oldResume.setCellType(HSSFCell.CELL_TYPE_FORMULA);
            oldResume.setCellFormula("SUM(K2:K" + numberRow + ")");
            oldResume.setCellStyle(footerStyle);
        }
        return workBook;
    }


    @Override
    public HSSFWorkbook reportProfit(Calendar start, Calendar end) {
        HSSFWorkbook workBook = new HSSFWorkbook();
        for (String series : garagDAO.getSeries()) {
            HSSFSheet sheet = workBook.createSheet(series);
            CellStyle headerStyle = workBook.createCellStyle();
            headerStyle.setWrapText(true);
            headerStyle.setAlignment(CellStyle.ALIGN_CENTER);
            Font font = workBook.createFont();
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            headerStyle.setFont(font);
            HSSFRow rowZero = sheet.createRow(0);
            HSSFRow rowOne = sheet.createRow(1);
            HSSFRow rowTwo = sheet.createRow(2);
            HSSFCell countHCell = rowZero.createCell(0);
            countHCell.setCellValue("№ п/п");
            countHCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 0));
            HSSFCell seriesHCell = rowZero.createCell(1);
            seriesHCell.setCellValue("№ ряда");
            seriesHCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 2, 1, 1));
            HSSFCell nubmerHCell = rowZero.createCell(2);
            nubmerHCell.setCellValue("№ гаража");
            nubmerHCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 2, 2, 2));
            HSSFCell fioHCell = rowZero.createCell(3);
            fioHCell.setCellValue("ФИО");
            fioHCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 2, 3, 3));
            HSSFCell allCHCell = rowZero.createCell(4);
            allCHCell.setCellValue("Задолженность");
            allCHCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 7));
            HSSFCell allSumCHCell = rowOne.createCell(4);
            allSumCHCell.setCellValue("Всего");
            allSumCHCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(1, 2, 4, 4));
            HSSFCell divedeCHCell = rowOne.createCell(5);
            divedeCHCell.setCellValue("в том числе");
            divedeCHCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 5, 7));
            HSSFCell contributeCHCell = rowTwo.createCell(5);
            contributeCHCell.setCellValue("членский взнос");
            contributeCHCell.setCellStyle(headerStyle);
            HSSFCell contLandCHCell = rowTwo.createCell(6);
            contLandCHCell.setCellValue("аренда земли");
            contLandCHCell.setCellStyle(headerStyle);
            HSSFCell contTargCHCell = rowTwo.createCell(7);
            contTargCHCell.setCellValue("целевой взнос");
            contTargCHCell.setCellStyle(headerStyle);
            HSSFCell allRHCell = rowZero.createCell(8);
            allRHCell.setCellValue("Начисленно");
            allRHCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 8, 11));
            HSSFCell allSumRHCell = rowOne.createCell(8);
            allSumRHCell.setCellValue("Всего");
            allSumRHCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(1, 2, 8, 8));
            HSSFCell divedeRHCell = rowOne.createCell(10);
            divedeRHCell.setCellValue("в том числе");
            divedeRHCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 9, 11));
            HSSFCell contributeRHCell = rowTwo.createCell(9);
            contributeRHCell.setCellValue("членский взнос");
            contributeRHCell.setCellStyle(headerStyle);
            HSSFCell contLandRHCell = rowTwo.createCell(10);
            contLandRHCell.setCellValue("аренда земли");
            contLandRHCell.setCellStyle(headerStyle);
            HSSFCell contTargRHCell = rowTwo.createCell(11);
            contTargRHCell.setCellValue("целевой взнос");
            contTargRHCell.setCellStyle(headerStyle);
            HSSFCell countTWOHCell = rowZero.createCell(12);
            countTWOHCell.setCellValue("№ п/п");
            countTWOHCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 2, 12, 12));
            HSSFCell allPHCell = rowZero.createCell(13);
            allPHCell.setCellValue("Уплачено");
            allPHCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 13, 20));
            HSSFCell numberPHCell = rowOne.createCell(13);
            numberPHCell.setCellValue("№");
            numberPHCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(1, 2, 13, 13));
            HSSFCell datePHCell = rowOne.createCell(14);
            datePHCell.setCellValue("Дата");
            datePHCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(1, 2, 14, 14));
            HSSFCell allSumPHCell = rowOne.createCell(15);
            allSumPHCell.setCellValue("Всего");
            allSumPHCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(1, 2, 15, 15));
            HSSFCell divedePHCell = rowOne.createCell(16);
            divedePHCell.setCellValue("в том числе");
            divedePHCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 16, 21));
            HSSFCell contributePHCell = rowTwo.createCell(16);
            contributePHCell.setCellValue("членский взнос");
            contributePHCell.setCellStyle(headerStyle);
            HSSFCell contLandPHCell = rowTwo.createCell(17);
            contLandPHCell.setCellValue("аренда земли");
            contLandPHCell.setCellStyle(headerStyle);
            HSSFCell contTargPHCell = rowTwo.createCell(18);
            contTargPHCell.setCellValue("целевой взнос");
            contTargPHCell.setCellStyle(headerStyle);
            HSSFCell assignPHCell = rowTwo.createCell(19);
            assignPHCell.setCellValue("дополнительный взнос");
            assignPHCell.setCellStyle(headerStyle);
            HSSFCell finesPHCell = rowTwo.createCell(20);
            finesPHCell.setCellValue("пени");
            finesPHCell.setCellStyle(headerStyle);
            HSSFCell balancePHCell = rowTwo.createCell(21);
            balancePHCell.setCellValue("остаток");
            balancePHCell.setCellStyle(headerStyle);
            HSSFCell allCNHCell = rowZero.createCell(22);
            allCNHCell.setCellValue("Задолжность");
            allCNHCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 22, 26));
            HSSFCell allSumCNHCell = rowOne.createCell(22);
            allSumCNHCell.setCellValue("Всего");
            allSumCNHCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(1, 2, 22, 22));
            HSSFCell divedeCNHCell = rowOne.createCell(23);
            divedeCNHCell.setCellValue("в том числе");
            divedeCNHCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 23, 26));
            HSSFCell contributeCNHCell = rowTwo.createCell(23);
            contributeCNHCell.setCellValue("членский взнос");
            contributeCNHCell.setCellStyle(headerStyle);
            HSSFCell contLandCNHCell = rowTwo.createCell(24);
            contLandCNHCell.setCellValue("аренда земли");
            contLandCNHCell.setCellStyle(headerStyle);
            HSSFCell contTargCNHCell = rowTwo.createCell(25);
            contTargCNHCell.setCellValue("целевой взнос");
            contTargCNHCell.setCellStyle(headerStyle);
            HSSFCell finesCNHCell = rowTwo.createCell(26);
            finesCNHCell.setCellValue("пени");
            finesCNHCell.setCellStyle(headerStyle);

            //Расчеты
            int fistRow = 3;
            int lastRow;
            int number = 1;
            List<Garag> garagList = garagDAO.findBySeriesAndPerson(series);
            Collections.sort(garagList, new GaragComparator());
            Integer year = start.get(Calendar.YEAR);
            for (Garag g : garagList) {
                List<Payment> paymentsPeriod = new ArrayList<Payment>();
                for (Payment payment : g.getPayments()) {
                    Calendar datePay = payment.getDatePayment();
                    if (datePay.getTimeInMillis() >= start.getTimeInMillis() && datePay.getTimeInMillis() <= end.getTimeInMillis()) {
                        paymentsPeriod.add(payment);
                    }
                }
                lastRow = (paymentsPeriod.size() == 0) ? fistRow : fistRow + paymentsPeriod.size() - 1;
                HSSFRow row = sheet.createRow(fistRow);
                HSSFCell countCell = row.createCell(0);
                countCell.setCellValue(number);
                sheet.addMergedRegion(new CellRangeAddress(fistRow, lastRow, 0, 0));
                HSSFCell seriesCell = row.createCell(1);
                seriesCell.setCellValue(g.getSeries());
                sheet.addMergedRegion(new CellRangeAddress(fistRow, lastRow, 1, 1));
                HSSFCell nubmerCell = row.createCell(2);
                nubmerCell.setCellValue(g.getNumber());
                sheet.addMergedRegion(new CellRangeAddress(fistRow, lastRow, 2, 2));
                HSSFCell fioCell = row.createCell(3);
                fioCell.setCellValue(g.getPerson().getFIO());
                sheet.addMergedRegion(new CellRangeAddress(fistRow, lastRow, 3, 3));
                //Задолжность до оплат выбранного года
                float oldContribute = 0f;
                float oldContLand = 0f;
                float oldContTarget = 0f;
                //Задолжность после оплат выбранного года
                float newContribute = 0f;
                float newContLand = 0f;
                float newContTarget = 0f;
                int newFines = 0;
                Contribution contribution = null;
                for (Contribution c : g.getContributions()) {
                    if (c.getYear() <= year) {
                        newContribute += c.getContribute();
                        newContLand += c.getContLand();
                        newContTarget += c.getContTarget();
                        newFines += c.getFines();
                    }
                    if (c.getYear().equals(year)) {
                        contribution = c;
                    }
                }
                Float sumContributions = newContribute + newContLand + newContTarget + newFines;
                Rent rent = rentService.findByYear(year);
                float rentSum = rent.getContributeMax() + rent.getContTargetMax();
                rentSum += (contribution.isBenefitsOn()) ? rent.getContLandMax() / 2 : rent.getContLandMax();
                for (Payment payment : paymentsPeriod) {
                    oldContribute += payment.getContributePay();
                    oldContLand += payment.getContLandPay();
                    oldContTarget += payment.getContTargetPay();
                }
                oldContribute += newContribute - rent.getContributeMax();
                if (contribution.isBenefitsOn()) {
                    oldContLand += newContLand - (rent.getContLandMax() / 2);
                } else {
                    oldContLand += newContLand - rent.getContLandMax();
                }
                oldContTarget += newContTarget - rent.getContTargetMax();
                float sumOldContribute = oldContribute + oldContLand + oldContTarget;
                //Сумма прошлой задолжности
                HSSFCell oldContributeSumCell = row.createCell(4);
                oldContributeSumCell.setCellValue(new BigDecimal(sumOldContribute).setScale(2, RoundingMode.UP).floatValue());
                sheet.addMergedRegion(new CellRangeAddress(fistRow, lastRow, 4, 4));
                //Прошлая задолжность по членскому взносу
                HSSFCell oldContributeCell = row.createCell(5);
                oldContributeCell.setCellValue(oldContribute);
                sheet.addMergedRegion(new CellRangeAddress(fistRow, lastRow, 5, 5));
                //Прошлая задолжность по аренде
                HSSFCell oldContLandCell = row.createCell(6);
                oldContLandCell.setCellValue(oldContLand);
                sheet.addMergedRegion(new CellRangeAddress(fistRow, lastRow, 6, 6));
                //Прошлая задолжность по целевому взносу
                HSSFCell oldContTargetCell = row.createCell(7);
                oldContTargetCell.setCellValue(oldContTarget);
                sheet.addMergedRegion(new CellRangeAddress(fistRow, lastRow, 7, 7));
                //Начисления текущего года
                HSSFCell rentSumCell = row.createCell(8);
                rentSumCell.setCellValue(rentSum);
                sheet.addMergedRegion(new CellRangeAddress(fistRow, lastRow, 8, 8));
                //Начисления текущего года по членскому взносу
                HSSFCell rentContributeCell = row.createCell(9);
                rentContributeCell.setCellValue(rent.getContributeMax());
                sheet.addMergedRegion(new CellRangeAddress(fistRow, lastRow, 9, 9));
                //Начисления текущего года по аренде земли
                HSSFCell rentContLandCell = row.createCell(10);
                if (contribution.isBenefitsOn()) {
                    rentContLandCell.setCellValue(rent.getContLandMax() / 2);
                } else {
                    rentContLandCell.setCellValue(rent.getContLandMax());
                }
                sheet.addMergedRegion(new CellRangeAddress(fistRow, lastRow, 10, 10));
                //Начисления текущего года по целевому взносу
                HSSFCell rentContTargetCell = row.createCell(11);
                rentContTargetCell.setCellValue(rent.getContTargetMax());
                sheet.addMergedRegion(new CellRangeAddress(fistRow, lastRow, 11, 11));
                //Порядковый номер
                HSSFCell countTwoCell = row.createCell(12);
                countTwoCell.setCellValue(number);
                sheet.addMergedRegion(new CellRangeAddress(fistRow, lastRow, 12, 12));
                int t = fistRow;
                for (Payment p : paymentsPeriod) {
                    if (t == fistRow) {
                        HSSFCell numberPay = row.createCell(13);
                        numberPay.setCellValue(p.getNumber());
                        Date dt = p.getDatePayment().getTime();
                        DateFormat ndf = new SimpleDateFormat("dd.MM.yyyy");
                        String dateFull = ndf.format(dt);
                        //Дата платежа
                        HSSFCell datePay = row.createCell(14);
                        datePay.setCellValue(dateFull);
                        //Сумма платежа
                        HSSFCell sumPay = row.createCell(15);
                        sumPay.setCellValue(p.getContributePay() + p.getContLandPay() + p.getContTargetPay() +
                                p.getAdditionallyPay() + p.getFinesPay() + p.getPay());
                        //Платеж  по членскому взносу
                        HSSFCell contributePay = row.createCell(16);
                        contributePay.setCellValue(p.getContributePay());
                        //Платеж по аренде
                        HSSFCell contLandPay = row.createCell(17);
                        contLandPay.setCellValue(p.getContLandPay());
                        //Платеж по целевому взносу
                        HSSFCell contTargetPay = row.createCell(18);
                        contTargetPay.setCellValue(p.getContTargetPay());
                        //Платеж по добавочному взносу
                        HSSFCell addingPay = row.createCell(19);
                        addingPay.setCellValue(p.getAdditionallyPay());
                        //Платеж по пеням
                        HSSFCell finesPay = row.createCell(20);
                        finesPay.setCellValue(p.getFinesPay());
                        //Остаточные деньги
                        HSSFCell balancePay = row.createCell(21);
                        balancePay.setCellValue(p.getPay());
                    } else {
                        HSSFRow rowNext = sheet.createRow(t);
                        HSSFCell numberPay = rowNext.createCell(13);
                        numberPay.setCellValue(p.getNumber());
                        Date dt = p.getDatePayment().getTime();
                        DateFormat ndf = new SimpleDateFormat("dd.MM.yyyy");
                        String dateFull = ndf.format(dt);
                        //Дата платежа
                        HSSFCell datePay = rowNext.createCell(14);
                        datePay.setCellValue(dateFull);
                        //Сумма платежа
                        HSSFCell sumPay = rowNext.createCell(15);
                        sumPay.setCellValue(p.getContributePay() + p.getContLandPay() + p.getContTargetPay() +
                                p.getAdditionallyPay() + p.getFinesPay() + p.getPay());
                        //Платеж  по членскому взносу
                        HSSFCell contributePay = rowNext.createCell(16);
                        contributePay.setCellValue(p.getContributePay());
                        //Платеж по аренде
                        HSSFCell contLandPay = rowNext.createCell(17);
                        contLandPay.setCellValue(p.getContLandPay());
                        //Платеж по целевому взносу
                        HSSFCell contTargetPay = rowNext.createCell(18);
                        contTargetPay.setCellValue(p.getContTargetPay());
                        //Платеж по добавочному взносу
                        HSSFCell addingPay = rowNext.createCell(19);
                        addingPay.setCellValue(p.getAdditionallyPay());
                        //Платеж по пеням
                        HSSFCell finesPay = rowNext.createCell(20);
                        finesPay.setCellValue(p.getFinesPay());
                        //Остаточные деньги
                        HSSFCell balancePay = rowNext.createCell(21);
                        balancePay.setCellValue(p.getPay());
                    }
                    t++;
                }
                //Долг после оплат
                HSSFCell contributeSumNewCell = row.createCell(22);
                contributeSumNewCell.setCellValue(new BigDecimal(sumContributions).setScale(2, RoundingMode.UP).floatValue());
                sheet.addMergedRegion(new CellRangeAddress(fistRow, lastRow, 22, 22));
                //Долг по членскому взносу
                HSSFCell contributeNewCell = row.createCell(23);
                contributeNewCell.setCellValue(newContribute);
                sheet.addMergedRegion(new CellRangeAddress(fistRow, lastRow, 23, 23));
                //Долги по аренде
                HSSFCell contLandNewCell = row.createCell(24);
                contLandNewCell.setCellValue(newContLand);
                sheet.addMergedRegion(new CellRangeAddress(fistRow, lastRow, 24, 24));
                //Долг по целевому взносу
                HSSFCell contTargetNewCell = row.createCell(25);
                contTargetNewCell.setCellValue(newContTarget);
                sheet.addMergedRegion(new CellRangeAddress(fistRow, lastRow, 25, 25));
                //Долг по пеням
                HSSFCell contFinesNewCell = row.createCell(26);
                contFinesNewCell.setCellValue(newFines);
                sheet.addMergedRegion(new CellRangeAddress(fistRow, lastRow, 26, 26));
                if (paymentsPeriod.size() == 0) {
                    fistRow += 1;
                }
                fistRow += paymentsPeriod.size();
                number++;
            }

            //FOOTER
            CellStyle footerStyle = workBook.createCellStyle();
            footerStyle.setWrapText(true);
            footerStyle.setAlignment(CellStyle.ALIGN_CENTER);
            footerStyle.setBorderTop(CellStyle.BORDER_MEDIUM);
            Font fontFooter = workBook.createFont();
            fontFooter.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            footerStyle.setFont(fontFooter);
            HSSFRow resumeRow = sheet.createRow(fistRow);
            HSSFCell resume = resumeRow.createCell(3);
            resume.setCellValue("Итого:");
            resume.setCellStyle(footerStyle);
            HSSFCell allContributeOld = resumeRow.createCell(4);
            allContributeOld.setCellType(HSSFCell.CELL_TYPE_FORMULA);
            allContributeOld.setCellFormula("SUM(E4:E" + fistRow + ")");
            allContributeOld.setCellStyle(footerStyle);

            HSSFCell contributeOld = resumeRow.createCell(5);
            contributeOld.setCellType(HSSFCell.CELL_TYPE_FORMULA);
            contributeOld.setCellFormula("SUM(F4:F" + fistRow + ")");
            contributeOld.setCellStyle(footerStyle);

            HSSFCell landOld = resumeRow.createCell(6);
            landOld.setCellType(HSSFCell.CELL_TYPE_FORMULA);
            landOld.setCellFormula("SUM(G4:G" + fistRow + ")");
            landOld.setCellStyle(footerStyle);

            HSSFCell tagetOld = resumeRow.createCell(7);
            tagetOld.setCellType(HSSFCell.CELL_TYPE_FORMULA);
            tagetOld.setCellFormula("SUM(H4:H" + fistRow + ")");
            tagetOld.setCellStyle(footerStyle);

            HSSFCell allRent = resumeRow.createCell(8);
            allRent.setCellType(HSSFCell.CELL_TYPE_FORMULA);
            allRent.setCellFormula("SUM(I4:I" + fistRow + ")");
            allRent.setCellStyle(footerStyle);

            HSSFCell contributeRent = resumeRow.createCell(9);
            contributeRent.setCellType(HSSFCell.CELL_TYPE_FORMULA);
            contributeRent.setCellFormula("SUM(J4:J" + fistRow + ")");
            contributeRent.setCellStyle(footerStyle);

            HSSFCell landRent = resumeRow.createCell(10);
            landRent.setCellType(HSSFCell.CELL_TYPE_FORMULA);
            landRent.setCellFormula("SUM(K4:K" + fistRow + ")");
            landRent.setCellStyle(footerStyle);

            HSSFCell tagetRent = resumeRow.createCell(11);
            tagetRent.setCellType(HSSFCell.CELL_TYPE_FORMULA);
            tagetRent.setCellFormula("SUM(L4:L" + fistRow + ")");
            tagetRent.setCellStyle(footerStyle);

            HSSFCell allPay = resumeRow.createCell(15);
            allPay.setCellType(HSSFCell.CELL_TYPE_FORMULA);
            allPay.setCellFormula("SUM(P4:P" + fistRow + ")");
            allPay.setCellStyle(footerStyle);

            HSSFCell contributePay = resumeRow.createCell(16);
            contributePay.setCellType(HSSFCell.CELL_TYPE_FORMULA);
            contributePay.setCellFormula("SUM(Q4:Q" + fistRow + ")");
            contributePay.setCellStyle(footerStyle);

            HSSFCell landPay = resumeRow.createCell(17);
            landPay.setCellType(HSSFCell.CELL_TYPE_FORMULA);
            landPay.setCellFormula("SUM(R4:R" + fistRow + ")");
            landPay.setCellStyle(footerStyle);

            HSSFCell tagetPay = resumeRow.createCell(18);
            tagetPay.setCellType(HSSFCell.CELL_TYPE_FORMULA);
            tagetPay.setCellFormula("SUM(S4:S" + fistRow + ")");
            tagetPay.setCellStyle(footerStyle);

            HSSFCell assignPay = resumeRow.createCell(19);
            assignPay.setCellType(HSSFCell.CELL_TYPE_FORMULA);
            assignPay.setCellFormula("SUM(T4:T" + fistRow + ")");
            assignPay.setCellStyle(footerStyle);

            HSSFCell finesPay = resumeRow.createCell(20);
            finesPay.setCellType(HSSFCell.CELL_TYPE_FORMULA);
            finesPay.setCellFormula("SUM(U4:U" + fistRow + ")");
            finesPay.setCellStyle(footerStyle);

            HSSFCell balance = resumeRow.createCell(21);
            balance.setCellType(HSSFCell.CELL_TYPE_FORMULA);
            balance.setCellFormula("SUM(V4:V" + fistRow + ")");
            balance.setCellStyle(footerStyle);

            HSSFCell allContributeNew = resumeRow.createCell(22);
            allContributeNew.setCellType(HSSFCell.CELL_TYPE_FORMULA);
            allContributeNew.setCellFormula("SUM(W4:W" + fistRow + ")");
            allContributeNew.setCellStyle(footerStyle);

            HSSFCell contributeNew = resumeRow.createCell(23);
            contributeNew.setCellType(HSSFCell.CELL_TYPE_FORMULA);
            contributeNew.setCellFormula("SUM(X4:X" + fistRow + ")");
            contributeNew.setCellStyle(footerStyle);

            HSSFCell landNew = resumeRow.createCell(24);
            landNew.setCellType(HSSFCell.CELL_TYPE_FORMULA);
            landNew.setCellFormula("SUM(Y4:Y" + fistRow + ")");
            landNew.setCellStyle(footerStyle);

            HSSFCell targetNew = resumeRow.createCell(25);
            targetNew.setCellType(HSSFCell.CELL_TYPE_FORMULA);
            targetNew.setCellFormula("SUM(Z4:Z" + fistRow + ")");
            targetNew.setCellStyle(footerStyle);

            HSSFCell finesNew = resumeRow.createCell(26);
            finesNew.setCellType(HSSFCell.CELL_TYPE_FORMULA);
            finesNew.setCellFormula("SUM(AA4:AA" + fistRow + ")");
            finesNew.setCellStyle(footerStyle);
        }
        return workBook;
    }
}

