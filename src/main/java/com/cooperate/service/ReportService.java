package com.cooperate.service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.Calendar;

/**
 * Created by Кирилл on 31.03.2017.
 */
public interface ReportService {

    HSSFWorkbook reportPayments(Calendar start, Calendar end);

    HSSFWorkbook reportAll();

    HSSFWorkbook reportBenefitsPerson();

    HSSFWorkbook reportContribute();

    HSSFWorkbook reportProfit(Calendar start, Calendar end);

}
