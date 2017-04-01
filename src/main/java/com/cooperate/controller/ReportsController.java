package com.cooperate.controller;

import com.cooperate.editor.CalendarCustomEditor;
import com.cooperate.service.*;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Controller
public class ReportsController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RentService rentService;

    private final Logger logger = Logger.getLogger(ReportsController.class);

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Calendar.class, new CalendarCustomEditor());
    }


    //Страница отчетов
    @RequestMapping(value = "reportOther", method = RequestMethod.GET)
    public String reportOther(ModelMap map) {
        map.addAttribute("rents", rentService.getRents());
        map.addAttribute("years", paymentService.findYears());
        return "report";
    }

    //Отчет - общий список гаражей
    @RequestMapping(method = RequestMethod.GET, value = "reportAllPerson")
    public String reportAllPerson(HttpServletResponse response, ModelMap map) {
        HSSFWorkbook workBook = reportService.reportAll();
        String filename = "Общий список";
        String URLEncodedFileName;
        try {
            URLEncodedFileName = URLEncoder.encode(filename, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        String resultFileName = URLEncodedFileName.replace('+', ' ');
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        dateFormat.setLenient(false);
        String strDate = dateFormat.format(new Date().getTime());
        resultFileName += "(" + strDate + ").xls";
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + resultFileName + "\"");
        try {
            ServletOutputStream os = response.getOutputStream();
            workBook.write(os);
            os.flush();
            os.close();
            logger.info("Сформирован отчет  - Общий список ");
        } catch (IOException e) {
            map.addAttribute("errMessage", "Ошибка отправки отчета");
            return "error";
        }
        return null;
    }

    //Отчет -  список льготников
    @RequestMapping(method = RequestMethod.GET, value = "reportBenefitsPerson")
    public String reportBenefitsPerson(HttpServletResponse response, ModelMap map) {
        HSSFWorkbook workBook = reportService.reportBenefitsPerson();
        String filename = "Список льготников";
        String URLEncodedFileName;
        try {
            URLEncodedFileName = URLEncoder.encode(filename, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        String resultFileName = URLEncodedFileName.replace('+', ' ');
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        dateFormat.setLenient(false);
        String strDate = dateFormat.format(new Date().getTime());
        resultFileName += "(" + strDate + ").xls";
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + resultFileName + "\"");
        try {
            ServletOutputStream os = response.getOutputStream();
            workBook.write(os);
            os.flush();
            os.close();
            logger.info("Сформирован отчет  - Список льготников ");
        } catch (IOException e) {
            map.addAttribute("errMessage", "Ошибка отправки отчета");
            return "error";
        }
        return null;
    }

    //Отчет -  список должников
    @RequestMapping(method = RequestMethod.GET, value = "reportContribute")
    public String reportContribute(HttpServletResponse response, ModelMap map) {
        HSSFWorkbook workBook = reportService.reportContribute();
        String filename = "Список должников";
        String URLEncodedFileName;
        try {
            URLEncodedFileName = URLEncoder.encode(filename, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        dateFormat.setLenient(false);
        String strDate = dateFormat.format(new Date().getTime());
        String resultFileName = URLEncodedFileName.replace('+', ' ');
        resultFileName += "(" + strDate + ").xls";
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + resultFileName + "\"");
        try {
            ServletOutputStream os = response.getOutputStream();
            workBook.write(os);
            os.flush();
            os.close();
            logger.info("Сформирован отчет  - Список должников ");
        } catch (IOException e) {
            map.addAttribute("errMessage", "Ошибка отправки отчета");
            return "error";
        }
        return null;
    }


    //Отчет -  доходы
    @RequestMapping(method = RequestMethod.GET, value = "reportProfit")
    public String reportProfit(@RequestParam("profitDateStart") Calendar dateStart,
                               @RequestParam("profitDateEnd") Calendar dateEnd,

                               HttpServletResponse response, ModelMap map) throws IOException, ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        dateFormat.setLenient(false);
        HSSFWorkbook workBook = reportService.reportProfit(dateStart, dateEnd);
        String filename = "Отчет по доходам";
        String URLEncodedFileName;
        try {
            URLEncodedFileName = URLEncoder.encode(filename, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        String resultFileName = URLEncodedFileName.replace('+', ' ');
        resultFileName += "(" + dateFormat.format(dateStart.getTime()) + "-" + dateFormat.format(dateEnd.getTime()) + ").xls";
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + resultFileName + "\"");
        try {
            ServletOutputStream os = response.getOutputStream();
            workBook.write(os);
            os.flush();
            os.close();
            logger.info("Сформирован отчет  - ДОХОДЫ ");
        } catch (IOException e) {
            map.put("message", "Ошибка отправки отчета");
            response.setStatus(409);
            return "error";
        }
        return null;
    }

    //Отчет по платежам
    @RequestMapping(method = RequestMethod.GET, value = "reportPayments")
    public String reportPayments(@RequestParam("paymentDateStart") Calendar dateStart,
                                 @RequestParam("paymentDateEnd") Calendar dateEnd,
                                 HttpServletResponse response, ModelMap map) throws IOException, ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        dateFormat.setLenient(false);
        HSSFWorkbook workBook = reportService.reportPayments(dateStart, dateEnd);
        String filename = "Отчет по платежам";
        String URLEncodedFileName;
        try {
            URLEncodedFileName = URLEncoder.encode(filename, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        String resultFileName = URLEncodedFileName.replace('+', ' ');
        resultFileName += "(" + dateFormat.format(dateStart.getTime()) + "-" + dateFormat.format(dateEnd.getTime()) + ").xls";
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + resultFileName + "\"");
        try {
            ServletOutputStream os = response.getOutputStream();
            workBook.write(os);
            os.flush();
            os.close();
            logger.info("Сформирован отчет  - ПЛАТЕЖИ ");
        } catch (IOException e) {
            map.put("message", "Ошибка отправки отчета");
            response.setStatus(409);
            return "error";
        }
        return null;
    }

}
