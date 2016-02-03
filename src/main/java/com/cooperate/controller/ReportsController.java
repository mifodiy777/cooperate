package com.cooperate.controller;

import com.cooperate.service.GaragService;
import com.cooperate.service.JournalHistoryService;
import com.cooperate.service.PaymentService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class ReportsController {

    @Autowired
    private GaragService garagService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private JournalHistoryService journalService;

    //Отчет - общий список гаражей
    @RequestMapping(method = RequestMethod.GET, value = "reportAllPerson")
    public String reportAllPerson(HttpServletResponse response, ModelMap map) {
        HSSFWorkbook workBook = garagService.reportAll();
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
            journalService.event("Сформирован отчет  - Общий список ");
        } catch (IOException e) {
            map.addAttribute("errMessage", "Ошибка отправки отчета");
            return "error";
        }
        return null;
    }

    //Отчет -  список льготников
    @RequestMapping(method = RequestMethod.GET, value = "reportBenefitsPerson")
    public String reportBenefitsPerson(HttpServletResponse response, ModelMap map) {
        HSSFWorkbook workBook = garagService.reportBenefitsPerson();
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
            journalService.event("Сформирован отчет  - Список льготников ");
        } catch (IOException e) {
            map.addAttribute("errMessage", "Ошибка отправки отчета");
            return "error";
        }
        return null;
    }

    //Отчет -  список должников
    @RequestMapping(method = RequestMethod.GET, value = "reportContribute")
    public String reportContribute(HttpServletResponse response, ModelMap map) {
        HSSFWorkbook workBook = garagService.reportContribute();
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
            journalService.event("Сформирован отчет  - Список должников ");
        } catch (IOException e) {
            map.addAttribute("errMessage", "Ошибка отправки отчета");
            return "error";
        }
        return null;
    }


    //Отчет -  доходы
    @RequestMapping(method = RequestMethod.GET, value = "reportProfit/{year}")
    public String reportProfit(@PathVariable("year") Integer year, HttpServletResponse response, ModelMap map) throws IOException {
        HSSFWorkbook workBook = garagService.reportProfit(year);
        String filename = "Отчет по доходам за" + year;
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
            journalService.event("Сформирован отчет  - ДОХОДЫ ");
        } catch (IOException e) {
            map.addAttribute("errMessage", "Ошибка отправки отчета");
            return "error";
        }
        return null;
    }

    //Отчет по платежам
    @RequestMapping(method = RequestMethod.GET, value = "reportPayments/{year}")
    public String reportPayments(@PathVariable("year") Integer year, HttpServletResponse response, ModelMap map) throws IOException {
        HSSFWorkbook workBook = paymentService.reportPayments(year) ;
        String filename = "Отчет по платежам за" + year;
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
            journalService.event("Сформирован отчет  - ПЛАТЕЖИ ");
        } catch (IOException e) {
            map.addAttribute("errMessage", "Ошибка отправки отчета");
            return "error";
        }
        return null;
    }

}
