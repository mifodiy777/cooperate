package com.cooperate.service;

import com.cooperate.comparator.GaragComparator;
import com.cooperate.dao.GaragDAO;
import com.cooperate.dao.PaymentDAO;
import com.cooperate.entity.Contribution;
import com.cooperate.entity.Garag;
import com.cooperate.entity.Payment;
import com.cooperate.entity.Rent;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Сервис формирования отчетов
 * Created by Кирилл on 31.03.2017.
 */
@Service
public class ReportService {

    @Autowired
    private GaragDAO garagDAO;

    @Autowired
    private RentService rentService;

    @Autowired
    private ContributionService contributionService;

    @Autowired
    private PaymentDAO paymentDAO;

    /**
     * Формирования отчета со списком платежей за определнный период
     * @param start начало периода
     * @param end конец периода
     * @return документ word
     */
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

    /**
     * Список всех гаражей
     * @return документ word
     */
    public HSSFWorkbook reportAll() {
        HSSFWorkbook workBook = new HSSFWorkbook();
        HSSFSheet sheet = workBook.createSheet("Список членов ГК");
        sheet.setActive(true);
        HSSFRow row = sheet.createRow(0);
        String[] hatCells = new String[]{"№", "Гараж", "ФИО", "Телефон", "Адрес", "Льготы"};
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
                sheet.setColumnWidth(i, (short) (30 * 256));
            }
        }
        int numberRow = 1;
        List<Garag> garagList = garagDAO.findAll();
        ;
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
                HSSFCell benefitsCell = nextRow.createCell(5);
                benefitsCell.setCellValue(garag.getPerson().getBenefits());
            }
            numberRow++;
        }
        return workBook;
    }

    /**
     * Список льготников
     * @return документ word
     */
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
        List<Garag> garagList = garagDAO.getGaragForPersonBenefits();
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

    /**
     * Список должников
     * @return документ word
     */
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
            Float sum = cont + land + target + fines + garag.getOldContribute();
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

    /**
     * Отчет о доходах за определнный период
     * @param start начало периода
     * @param end конец периода
     * @return документ word
     */
    public HSSFWorkbook reportProfit(Calendar start, Calendar end) {
        HSSFWorkbook workBook = new HSSFWorkbook();
        CellStyle style = workBook.createCellStyle();
        style.setWrapText(true);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        Font font = workBook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(font);
        for (String series : garagDAO.getSeries()) {
            HSSFSheet sheet = workBook.createSheet(series);
            createHeader(sheet, style);
            //Расчеты
            int fistRow = 3;
            int lastRow;
            //Порядковый номер гаража
            int number = 1;
            //Находим гаражи с назначенными владельцами
            List<Garag> garagList = garagDAO.findBySeriesAndPerson(series);
            //Сортируем по номеру гаража
            Collections.sort(garagList, new GaragComparator());
            //Период выбираеться по стартовой дате
            Integer year = start.get(Calendar.YEAR);
            Rent rent = rentService.findByYear(year);
            if (rent != null) {
                for (Garag g : garagList) {
                    //Находим платежи входящие в выбранный период для текущего гаража, заодно считаем их
                    List<Payment> paymentsPeriod = new ArrayList<Payment>();
                    for (Payment payment : g.getPayments()) {
                        Calendar datePay = payment.getDatePayment();
                        if (datePay.getTimeInMillis() >= start.getTimeInMillis() && datePay.getTimeInMillis() <= end.getTimeInMillis()) {
                            paymentsPeriod.add(payment);
                        }
                    }
                    //Из количества платежей определяем количество строк в таблице для данного гаража
                    lastRow = (paymentsPeriod.size() == 0) ? fistRow : fistRow + paymentsPeriod.size() - 1;
                    HSSFRow row = sheet.createRow(fistRow);
                    //Порядковый номер 1
                    HSSFCell countCell = row.createCell(0);
                    countCell.setCellValue(number);
                    sheet.addMergedRegion(new CellRangeAddress(fistRow, lastRow, 0, 0));
                    //Порядковый номер 2
                    HSSFCell countTwoCell = row.createCell(13);
                    countTwoCell.setCellValue(number);
                    sheet.addMergedRegion(new CellRangeAddress(fistRow, lastRow, 13, 13));
                    //Ряд
                    HSSFCell seriesCell = row.createCell(1);
                    seriesCell.setCellValue(g.getSeries());
                    sheet.addMergedRegion(new CellRangeAddress(fistRow, lastRow, 1, 1));
                    //Номер гаража
                    HSSFCell nubmerCell = row.createCell(2);
                    nubmerCell.setCellValue(g.getNumber());
                    sheet.addMergedRegion(new CellRangeAddress(fistRow, lastRow, 2, 2));
                    //ФИО владельца
                    HSSFCell fioCell = row.createCell(3);
                    fioCell.setCellValue(g.getPerson().getFIO());
                    sheet.addMergedRegion(new CellRangeAddress(fistRow, lastRow, 3, 3));
                    //Задолжность после оплат выбранного года
                    float contribute = 0f;
                    float contLand = 0f;
                    float contTarget = 0f;
                    float old = g.getOldContribute();
                    int fines = 0;
                    Contribution contribution = contributionService.getContributionByGaragAndYear(g.getId(), year);
                    for (Contribution c : g.getContributions()) {
                        if (c.getYear() <= year) {
                            contribute += c.getContribute();
                            contLand += c.getContLand();
                            contTarget += c.getContTarget();
                        }
                    }
                    Float sumContributions = contribute + contLand + contTarget + fines + old;
                    //Долг после оплат
                    HSSFCell contributeSumNewCell = row.createCell(24);
                    contributeSumNewCell.setCellValue(new BigDecimal(sumContributions).setScale(2, RoundingMode.UP).floatValue());
                    sheet.addMergedRegion(new CellRangeAddress(fistRow, lastRow, 24, 24));
                    //Долг по членскому взносу
                    HSSFCell contributeNewCell = row.createCell(25);
                    contributeNewCell.setCellValue(contribute);
                    sheet.addMergedRegion(new CellRangeAddress(fistRow, lastRow, 25, 25));
                    //Долги по аренде
                    HSSFCell contLandNewCell = row.createCell(26);
                    contLandNewCell.setCellValue(contLand);
                    sheet.addMergedRegion(new CellRangeAddress(fistRow, lastRow, 26, 26));
                    //Долг по целевому взносу
                    HSSFCell contTargetNewCell = row.createCell(27);
                    contTargetNewCell.setCellValue(contTarget);
                    sheet.addMergedRegion(new CellRangeAddress(fistRow, lastRow, 27, 27));
                    //Долг по прошлым годам
                    HSSFCell contOldNewCell = row.createCell(28);
                    contOldNewCell.setCellValue(old);
                    sheet.addMergedRegion(new CellRangeAddress(fistRow, lastRow, 28, 28));
                    //Долг по пеням
                    HSSFCell contFinesNewCell = row.createCell(29);
                    contFinesNewCell.setCellValue(fines);
                    sheet.addMergedRegion(new CellRangeAddress(fistRow, lastRow, 29, 29));
                    //Начисления текущего года
                    float rentSum = 0f;
                    float rentContribute = 0f;
                    float rentContLand = 0f;
                    float rentContTarget = 0f;
                    if (contribution != null) {
                        rentContribute = (!contribution.isMemberBoardOn()) ? rent.getContributeMax() : 0;
                        rentContLand = (contribution.isBenefitsOn()) ? rent.getContLandMax() / 2 : rent.getContLandMax();
                        rentContTarget = rent.getContTargetMax();
                        rentSum = rentContribute + rentContLand + rentContTarget;
                    }
                    //Сумма начислений
                    HSSFCell rentSumCell = row.createCell(9);
                    rentSumCell.setCellValue(rentSum);
                    sheet.addMergedRegion(new CellRangeAddress(fistRow, lastRow, 9, 9));
                    //Начисления членского взноса
                    HSSFCell rentContributeCell = row.createCell(10);
                    rentContributeCell.setCellValue(rentContribute);
                    sheet.addMergedRegion(new CellRangeAddress(fistRow, lastRow, 10, 10));
                    //Начисления аренды земли
                    HSSFCell rentContLandCell = row.createCell(11);
                    rentContLandCell.setCellValue(rentContLand);
                    sheet.addMergedRegion(new CellRangeAddress(fistRow, lastRow, 11, 11));
                    //Начисления целевого взноса
                    HSSFCell rentContTargetCell = row.createCell(12);
                    rentContTargetCell.setCellValue(rentContTarget);
                    sheet.addMergedRegion(new CellRangeAddress(fistRow, lastRow, 12, 12));
                    //Приход
                    int t = fistRow;
                    for (Payment p : paymentsPeriod) {
                        if (t == fistRow) {
                            calculatePayment(row, p);
                        } else {
                            HSSFRow rowNext = sheet.createRow(t);
                            calculatePayment(rowNext, p);
                        }
                        t++;
                    }
                    //Задолжность до оплат выбранного года
                    float pastContribute = 0f;
                    float pastContLand = 0f;
                    float pastContTarget = 0f;
                    float pastOld = 0f;
                    for (Payment payment : paymentsPeriod) {
                        pastContribute += payment.getContributePay();
                        pastContLand += payment.getContLandPay();
                        pastContTarget += payment.getContTargetPay();
                        pastOld += payment.getOldContributePay();
                    }
                    pastContribute += contribute - rentContribute;
                    pastContLand += contLand - rentContLand;
                    pastContTarget += contTarget - rentContTarget;
                    pastOld += old;
                    float sumOldContribute = pastContribute + pastContLand + pastContTarget + pastOld;
                    //Сумма прошлой задолжности
                    HSSFCell oldContributeSumCell = row.createCell(4);
                    oldContributeSumCell.setCellValue(new BigDecimal(sumOldContribute).setScale(2, RoundingMode.UP).floatValue());
                    sheet.addMergedRegion(new CellRangeAddress(fistRow, lastRow, 4, 4));
                    //Прошлая задолжность по членскому взносу
                    HSSFCell oldContributeCell = row.createCell(5);
                    oldContributeCell.setCellValue(pastContribute);
                    sheet.addMergedRegion(new CellRangeAddress(fistRow, lastRow, 5, 5));
                    //Прошлая задолжность по аренде
                    HSSFCell oldContLandCell = row.createCell(6);
                    oldContLandCell.setCellValue(pastContLand);
                    sheet.addMergedRegion(new CellRangeAddress(fistRow, lastRow, 6, 6));
                    //Прошлая задолжность по целевому взносу
                    HSSFCell oldContTargetCell = row.createCell(7);
                    oldContTargetCell.setCellValue(pastContTarget);
                    sheet.addMergedRegion(new CellRangeAddress(fistRow, lastRow, 7, 7));
                    //Прошлая задолжность по долгам прошлых лет
                    HSSFCell pastOldCell = row.createCell(8);
                    pastOldCell.setCellValue(pastOld);
                    sheet.addMergedRegion(new CellRangeAddress(fistRow, lastRow, 8, 8));
                    //Счетчики
                    if (paymentsPeriod.size() == 0) {
                        fistRow += 1;
                    }
                    fistRow += paymentsPeriod.size();
                    number++;
                }
            }
            //Суммы
            createFooter(sheet, style, fistRow);

        }
        return workBook;
    }

    private void createHeader(HSSFSheet sheet, CellStyle style) {
        HSSFRow rowZero = sheet.createRow(0);
        HSSFRow rowOne = sheet.createRow(1);
        HSSFRow rowTwo = sheet.createRow(2);
        HSSFCell countHCell = rowZero.createCell(0);
        countHCell.setCellValue("№ п/п");
        countHCell.setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 0));
        HSSFCell seriesHCell = rowZero.createCell(1);
        seriesHCell.setCellValue("№ ряда");
        seriesHCell.setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress(0, 2, 1, 1));
        HSSFCell nubmerHCell = rowZero.createCell(2);
        nubmerHCell.setCellValue("№ гаража");
        nubmerHCell.setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress(0, 2, 2, 2));
        HSSFCell fioHCell = rowZero.createCell(3);
        fioHCell.setCellValue("ФИО");
        fioHCell.setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress(0, 2, 3, 3));
        HSSFCell allCHCell = rowZero.createCell(4);
        allCHCell.setCellValue("Задолженность");
        allCHCell.setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 7));
        HSSFCell allSumCHCell = rowOne.createCell(4);
        allSumCHCell.setCellValue("Всего");
        allSumCHCell.setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress(1, 2, 4, 4));
        HSSFCell divedeCHCell = rowOne.createCell(5);
        divedeCHCell.setCellValue("в том числе");
        divedeCHCell.setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 5, 7));
        HSSFCell contributeCHCell = rowTwo.createCell(5);
        contributeCHCell.setCellValue("членский взнос");
        contributeCHCell.setCellStyle(style);
        HSSFCell contLandCHCell = rowTwo.createCell(6);
        contLandCHCell.setCellValue("аренда земли");
        contLandCHCell.setCellStyle(style);
        HSSFCell contTargCHCell = rowTwo.createCell(7);
        contTargCHCell.setCellValue("целевой взнос");
        contTargCHCell.setCellStyle(style);
        HSSFCell oldCHCell = rowTwo.createCell(8);
        oldCHCell.setCellValue("долги прошлых лет");
        oldCHCell.setCellStyle(style);
        HSSFCell allRHCell = rowZero.createCell(9);
        allRHCell.setCellValue("Начисленно");
        allRHCell.setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 9, 12));
        HSSFCell allSumRHCell = rowOne.createCell(9);
        allSumRHCell.setCellValue("Всего");
        allSumRHCell.setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress(1, 2, 9, 9));
        HSSFCell divedeRHCell = rowOne.createCell(10);
        divedeRHCell.setCellValue("в том числе");
        divedeRHCell.setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 10, 12));
        HSSFCell contributeRHCell = rowTwo.createCell(10);
        contributeRHCell.setCellValue("членский взнос");
        contributeRHCell.setCellStyle(style);
        HSSFCell contLandRHCell = rowTwo.createCell(11);
        contLandRHCell.setCellValue("аренда земли");
        contLandRHCell.setCellStyle(style);
        HSSFCell contTargRHCell = rowTwo.createCell(12);
        contTargRHCell.setCellValue("целевой взнос");
        contTargRHCell.setCellStyle(style);
        HSSFCell countTWOHCell = rowZero.createCell(13);
        countTWOHCell.setCellValue("№ п/п");
        countTWOHCell.setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress(0, 2, 13, 13));
        HSSFCell allPHCell = rowZero.createCell(14);
        allPHCell.setCellValue("Уплачено");
        allPHCell.setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 14, 23));
        HSSFCell numberPHCell = rowOne.createCell(14);
        numberPHCell.setCellValue("№");
        numberPHCell.setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress(1, 2, 14, 14));
        HSSFCell datePHCell = rowOne.createCell(15);
        datePHCell.setCellValue("Дата");
        datePHCell.setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress(1, 2, 15, 15));
        HSSFCell allSumPHCell = rowOne.createCell(16);
        allSumPHCell.setCellValue("Всего");
        allSumPHCell.setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress(1, 2, 16, 16));
        HSSFCell divedePHCell = rowOne.createCell(17);
        divedePHCell.setCellValue("в том числе");
        divedePHCell.setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 17, 23));
        HSSFCell contributePHCell = rowTwo.createCell(17);
        contributePHCell.setCellValue("членский взнос");
        contributePHCell.setCellStyle(style);
        HSSFCell contLandPHCell = rowTwo.createCell(18);
        contLandPHCell.setCellValue("аренда земли");
        contLandPHCell.setCellStyle(style);
        HSSFCell contTargPHCell = rowTwo.createCell(19);
        contTargPHCell.setCellValue("целевой взнос");
        contTargPHCell.setCellStyle(style);
        HSSFCell assignPHCell = rowTwo.createCell(20);
        assignPHCell.setCellValue("дополнительный взнос");
        assignPHCell.setCellStyle(style);
        HSSFCell oldPHCell = rowTwo.createCell(21);
        oldPHCell.setCellValue("долги прошлых лет");
        oldPHCell.setCellStyle(style);
        HSSFCell finesPHCell = rowTwo.createCell(22);
        finesPHCell.setCellValue("пени");
        finesPHCell.setCellStyle(style);
        HSSFCell balancePHCell = rowTwo.createCell(23);
        balancePHCell.setCellValue("остаток");
        balancePHCell.setCellStyle(style);
        HSSFCell allCNHCell = rowZero.createCell(24);
        allCNHCell.setCellValue("Задолжность");
        allCNHCell.setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 24, 29));
        HSSFCell allSumCNHCell = rowOne.createCell(24);
        allSumCNHCell.setCellValue("Всего");
        allSumCNHCell.setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress(1, 2, 24, 24));
        HSSFCell divedeCNHCell = rowOne.createCell(25);
        divedeCNHCell.setCellValue("в том числе");
        divedeCNHCell.setCellStyle(style);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 25, 29));
        HSSFCell contributeCNHCell = rowTwo.createCell(25);
        contributeCNHCell.setCellValue("членский взнос");
        contributeCNHCell.setCellStyle(style);
        HSSFCell contLandCNHCell = rowTwo.createCell(26);
        contLandCNHCell.setCellValue("аренда земли");
        contLandCNHCell.setCellStyle(style);
        HSSFCell contTargCNHCell = rowTwo.createCell(27);
        contTargCNHCell.setCellValue("целевой взнос");
        contTargCNHCell.setCellStyle(style);
        HSSFCell oldCNHCell = rowTwo.createCell(28);
        oldCNHCell.setCellValue("долги прошлых лет");
        oldCNHCell.setCellStyle(style);
        HSSFCell finesCNHCell = rowTwo.createCell(29);
        finesCNHCell.setCellValue("пени");
        finesCNHCell.setCellStyle(style);
    }

    private void createFooter(HSSFSheet sheet, CellStyle style, int numberRow) {
        HSSFRow resumeRow = sheet.createRow(numberRow);
        HSSFCell resume = resumeRow.createCell(3);
        resume.setCellValue("Итого:");
        resume.setCellStyle(style);
        HSSFCell allContributeOld = resumeRow.createCell(4);
        allContributeOld.setCellType(HSSFCell.CELL_TYPE_FORMULA);
        allContributeOld.setCellFormula("SUM(E4:E" + numberRow + ")");
        allContributeOld.setCellStyle(style);

        HSSFCell contributeOld = resumeRow.createCell(5);
        contributeOld.setCellType(HSSFCell.CELL_TYPE_FORMULA);
        contributeOld.setCellFormula("SUM(F4:F" + numberRow + ")");
        contributeOld.setCellStyle(style);

        HSSFCell landOld = resumeRow.createCell(6);
        landOld.setCellType(HSSFCell.CELL_TYPE_FORMULA);
        landOld.setCellFormula("SUM(G4:G" + numberRow + ")");
        landOld.setCellStyle(style);

        HSSFCell tagetOld = resumeRow.createCell(7);
        tagetOld.setCellType(HSSFCell.CELL_TYPE_FORMULA);
        tagetOld.setCellFormula("SUM(H4:H" + numberRow + ")");
        tagetOld.setCellStyle(style);

        HSSFCell pastOld = resumeRow.createCell(8);
        pastOld.setCellType(HSSFCell.CELL_TYPE_FORMULA);
        pastOld.setCellFormula("SUM(I4:I" + numberRow + ")");
        pastOld.setCellStyle(style);

        HSSFCell allRent = resumeRow.createCell(9);
        allRent.setCellType(HSSFCell.CELL_TYPE_FORMULA);
        allRent.setCellFormula("SUM(J4:J" + numberRow + ")");
        allRent.setCellStyle(style);

        HSSFCell contributeRent = resumeRow.createCell(10);
        contributeRent.setCellType(HSSFCell.CELL_TYPE_FORMULA);
        contributeRent.setCellFormula("SUM(K4:K" + numberRow + ")");
        contributeRent.setCellStyle(style);

        HSSFCell landRent = resumeRow.createCell(11);
        landRent.setCellType(HSSFCell.CELL_TYPE_FORMULA);
        landRent.setCellFormula("SUM(L4:L" + numberRow + ")");
        landRent.setCellStyle(style);

        HSSFCell tagetRent = resumeRow.createCell(12);
        tagetRent.setCellType(HSSFCell.CELL_TYPE_FORMULA);
        tagetRent.setCellFormula("SUM(M4:M" + numberRow + ")");
        tagetRent.setCellStyle(style);

        HSSFCell allPay = resumeRow.createCell(16);
        allPay.setCellType(HSSFCell.CELL_TYPE_FORMULA);
        allPay.setCellFormula("SUM(Q4:Q" + numberRow + ")");
        allPay.setCellStyle(style);

        HSSFCell contributePay = resumeRow.createCell(17);
        contributePay.setCellType(HSSFCell.CELL_TYPE_FORMULA);
        contributePay.setCellFormula("SUM(R4:R" + numberRow + ")");
        contributePay.setCellStyle(style);

        HSSFCell landPay = resumeRow.createCell(18);
        landPay.setCellType(HSSFCell.CELL_TYPE_FORMULA);
        landPay.setCellFormula("SUM(S4:S" + numberRow + ")");
        landPay.setCellStyle(style);

        HSSFCell tagetPay = resumeRow.createCell(19);
        tagetPay.setCellType(HSSFCell.CELL_TYPE_FORMULA);
        tagetPay.setCellFormula("SUM(T4:T" + numberRow + ")");
        tagetPay.setCellStyle(style);

        HSSFCell assignPay = resumeRow.createCell(20);
        assignPay.setCellType(HSSFCell.CELL_TYPE_FORMULA);
        assignPay.setCellFormula("SUM(U4:U" + numberRow + ")");
        assignPay.setCellStyle(style);

        HSSFCell oldPay = resumeRow.createCell(21);
        oldPay.setCellType(HSSFCell.CELL_TYPE_FORMULA);
        oldPay.setCellFormula("SUM(V4:V" + numberRow + ")");
        oldPay.setCellStyle(style);

        HSSFCell finesPay = resumeRow.createCell(22);
        finesPay.setCellType(HSSFCell.CELL_TYPE_FORMULA);
        finesPay.setCellFormula("SUM(W4:W" + numberRow + ")");
        finesPay.setCellStyle(style);

        HSSFCell balance = resumeRow.createCell(23);
        balance.setCellType(HSSFCell.CELL_TYPE_FORMULA);
        balance.setCellFormula("SUM(X4:X" + numberRow + ")");
        balance.setCellStyle(style);

        HSSFCell allContributeNew = resumeRow.createCell(24);
        allContributeNew.setCellType(HSSFCell.CELL_TYPE_FORMULA);
        allContributeNew.setCellFormula("SUM(Y4:Y" + numberRow + ")");
        allContributeNew.setCellStyle(style);

        HSSFCell contributeNew = resumeRow.createCell(25);
        contributeNew.setCellType(HSSFCell.CELL_TYPE_FORMULA);
        contributeNew.setCellFormula("SUM(Z4:Z" + numberRow + ")");
        contributeNew.setCellStyle(style);

        HSSFCell landNew = resumeRow.createCell(26);
        landNew.setCellType(HSSFCell.CELL_TYPE_FORMULA);
        landNew.setCellFormula("SUM(AA4:AA" + numberRow + ")");
        landNew.setCellStyle(style);

        HSSFCell targetNew = resumeRow.createCell(27);
        targetNew.setCellType(HSSFCell.CELL_TYPE_FORMULA);
        targetNew.setCellFormula("SUM(AB4:AB" + numberRow + ")");
        targetNew.setCellStyle(style);

        HSSFCell oldNew = resumeRow.createCell(28);
        oldNew.setCellType(HSSFCell.CELL_TYPE_FORMULA);
        oldNew.setCellFormula("SUM(AC4:AC" + numberRow + ")");
        oldNew.setCellStyle(style);

        HSSFCell finesNew = resumeRow.createCell(29);
        finesNew.setCellType(HSSFCell.CELL_TYPE_FORMULA);
        finesNew.setCellFormula("SUM(AD4:AD" + numberRow + ")");
        finesNew.setCellStyle(style);
    }

    private void calculatePayment(HSSFRow row, Payment p) {
        HSSFCell numberPay = row.createCell(14);
        numberPay.setCellValue(p.getNumber());
        //Дата платежа
        HSSFCell datePay = row.createCell(15);
        Date dt = p.getDatePayment().getTime();
        DateFormat ndf = new SimpleDateFormat("dd.MM.yyyy");
        datePay.setCellValue(ndf.format(dt));
        //Сумма платежа
        HSSFCell sumPay = row.createCell(16);
        sumPay.setCellValue(p.getContributePay() + p.getContLandPay() + p.getContTargetPay() +
                p.getAdditionallyPay() + p.getFinesPay() + p.getOldContributePay() + p.getPay());
        //Платеж  по членскому взносу
        HSSFCell contributePay = row.createCell(17);
        contributePay.setCellValue(p.getContributePay());
        //Платеж по аренде
        HSSFCell contLandPay = row.createCell(18);
        contLandPay.setCellValue(p.getContLandPay());
        //Платеж по целевому взносу
        HSSFCell contTargetPay = row.createCell(19);
        contTargetPay.setCellValue(p.getContTargetPay());
        //Платеж по добавочному взносу
        HSSFCell addingPay = row.createCell(20);
        addingPay.setCellValue(p.getAdditionallyPay());
        //Платеж по долгам прошлых лет
        HSSFCell oldPay = row.createCell(21);
        oldPay.setCellValue(p.getOldContributePay());
        //Платеж по пеням
        HSSFCell finesPay = row.createCell(22);
        finesPay.setCellValue(p.getFinesPay());
        //Остаточные деньги
        HSSFCell balancePay = row.createCell(23);
        balancePay.setCellValue(p.getPay());
    }
}
