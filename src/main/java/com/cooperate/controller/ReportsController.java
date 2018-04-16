package com.cooperate.controller;

import com.cooperate.editor.CalendarCustomEditor;
import com.cooperate.service.PaymentService;
import com.cooperate.service.RentService;
import com.cooperate.service.ReportService;
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
import java.util.Calendar;

import static com.cooperate.Utils.formatDate;

/**
 * Контроллер отчетов
 */
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

    /**
     * Страница дополнительных отчетов
     *
     * @param map ModelMap
     * @return report.jsp
     */
    @RequestMapping(value = "reportOther", method = RequestMethod.GET)
    public String reportOther(ModelMap map) {
        map.addAttribute("rents", rentService.getRents());
        map.addAttribute("years", paymentService.findYears());
        return "report";
    }

    /**
     * Отчет - общий список гаражей
     *
     * @param response
     * @param map
     * @return Отчет - общий список гаражей
     */
    @RequestMapping(method = RequestMethod.GET, value = "reportAllPerson")
    public String reportAllPerson(HttpServletResponse response, ModelMap map) {
        String fileName = fileName("Общий список");
        fileName += "(" + formatDate(Calendar.getInstance()) + ").xls";
        return report(reportService.reportAll(), fileName, response, map);
    }

    /**
     * Отчет -  список льготников
     *
     * @param response
     * @param map
     * @return Отчет -  список льготников
     */
    @RequestMapping(method = RequestMethod.GET, value = "reportBenefitsPerson")
    public String reportBenefitsPerson(HttpServletResponse response, ModelMap map) {
        String fileName = fileName("Список льготников");
        fileName += "(" + formatDate(Calendar.getInstance()) + ").xls";
        return report(reportService.reportBenefitsPerson(), fileName, response, map);
    }

    /**
     * Отчет -  список должников
     *
     * @param response
     * @param map
     * @return Отчет -  список должников
     */
    @RequestMapping(method = RequestMethod.GET, value = "reportContribute")
    public String reportContribute(HttpServletResponse response, ModelMap map) {
        String fileName = fileName("Список должников");
        fileName += "(" + formatDate(Calendar.getInstance()) + ").xls";
        return report(reportService.reportContribute(), fileName, response, map);
    }


    /**
     * Отчет -  доходы
     *
     * @param dateStart Дата начала периода отчета
     * @param dateEnd   Дата завершения периода отчета
     * @param response
     * @param map
     * @return отчет
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(method = RequestMethod.GET, value = "reportProfit")
    public String reportProfit(@RequestParam("profitDateStart") Calendar dateStart,
                               @RequestParam("profitDateEnd") Calendar dateEnd,
                               HttpServletResponse response, ModelMap map) throws IOException, ParseException {
        String fileName = fileName("Отчет по доходам");
        fileName += "(" + formatDate(dateStart) + "-" + formatDate(dateEnd) + ").xls";
        return report(reportService.reportProfit(dateStart, dateEnd), fileName, response, map);
    }

    /**
     * Отчет по платежам
     *
     * @param dateStart Дата начала периода отчета
     * @param dateEnd   Дата завершения периода отчета
     * @param response
     * @param map
     * @return отчет
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(method = RequestMethod.GET, value = "reportPayments")
    public String reportPayments(@RequestParam("paymentDateStart") Calendar dateStart,
                                 @RequestParam("paymentDateEnd") Calendar dateEnd,
                                 HttpServletResponse response, ModelMap map) throws IOException, ParseException {
        String fileName = fileName("Отчет по платежам");
        fileName += "(" + formatDate(dateStart) + "-" + formatDate(dateEnd) + ").xls";
        return report(reportService.reportPayments(dateStart, dateEnd), fileName, response, map);
    }

    /**
     * Отчет по типам расходов
     *
     * @param start    Дата начала периода отчета
     * @param end      Дата завершения периода отчета
     * @param response
     * @param map
     * @return отчет
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(method = RequestMethod.GET, value = "reportGroupCost")
    public String reportGroupCost(@RequestParam("costTypeDateStart") Calendar start,
                                  @RequestParam("costTypeDateEnd") Calendar end,
                                  HttpServletResponse response, ModelMap map) throws IOException, ParseException {
        String fileName = fileName("Отчет по типам расходов");
        fileName += "(" + formatDate(start) + "-" + formatDate(end) + ").xls";
        return report(reportService.reportGroupCost(start, end), fileName, response, map);
    }

    /**
     * Отчет по расходам
     *
     * @param start    Дата начала периода отчета
     * @param end      Дата завершения периода отчета
     * @param response
     * @param map
     * @return отчет
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(method = RequestMethod.GET, value = "reportCost")
    public String reportCost(@RequestParam("reportCostDateStart") Calendar start,
                             @RequestParam("reportCostDateEnd") Calendar end,
                             HttpServletResponse response, ModelMap map) throws IOException, ParseException {
        String fileName = fileName("Отчет по расходам");
        fileName += "(" + formatDate(start) + "-" + formatDate(end) + ").xls";
        return report(reportService.reportCost(start, end), fileName, response, map);
    }

    /**
     * Подготовка создания файла
     *
     * @param workbook excel
     * @param fileName имя файла
     * @param response ответ
     * @param map      мапа
     * @return ничего / страница с ошибкой
     */
    private String report(HSSFWorkbook workbook, String fileName,
                          HttpServletResponse response, ModelMap map) {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        try {
            ServletOutputStream os = response.getOutputStream();
            workbook.write(os);
            os.flush();
            os.close();
            logger.info("Сформирован отчет: " + fileName);
            return null;
        } catch (IOException e) {
            map.put("message", "Ошибка отправки отчета");
            response.setStatus(409);
            return "error";
        }
    }

    /**
     * Метод декодирования имя файла
     *
     * @param fileName имя файла
     * @return декодированное имя файла
     */
    private static String fileName(String fileName) {
        String encodeName;
        try {
            encodeName = URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        return encodeName.replace('+', ' ');
    }


}
