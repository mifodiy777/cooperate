package com.cooperate.service.impl;

import com.cooperate.entity.Address;
import com.cooperate.entity.Garag;
import com.cooperate.entity.Person;
import com.cooperate.service.FileParseService;
import com.cooperate.service.GaragService;
import com.cooperate.service.PersonService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

@Service
public class FileParseServiceImpl implements FileParseService {

    @Autowired
    private GaragService garagService;

    @Autowired
    private PersonService personService;

    //Парсер гаражей
    @Transactional
    public void parseGarag(MultipartFile file) {
        try {
            InputStream in = null;
            HSSFWorkbook wb = null;
            try {
                //Получаем файл
                in = file.getInputStream();
                wb = new HSSFWorkbook(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Sheet sheet = wb.getSheetAt(0);
            Iterator<Row> it = sheet.iterator();
            //Идем по строкам
            while (it.hasNext()) {
                //Создаем пустые объекты
                Garag garag = new Garag();
                Person person = new Person();
                Address address = new Address();
                Row row = it.next();
                Iterator<Cell> cells = row.iterator();
                //Идем по ячейкам
                while (cells.hasNext()) {
                    Cell cell = cells.next();
                    if (cell.getColumnIndex() == 0) {
                        cell.setCellType(1);
                        String[] garagNumber = cell.getStringCellValue().trim().split("-");
                        String series = garagNumber[0];
                        String number = garagNumber[1];
                        //Заполняем ряд и номер гаража
                        garag.setSeries(series);
                        garag.setNumber(number);
                    }
                    if (cell.getColumnIndex() == 1) {
                        cell.setCellType(1);
                        String[] namePerson = cell.getStringCellValue().trim().split(" ");
                        //Заполняем ФИО владельца
                        person.setLastName(namePerson[0]);
                        person.setName(namePerson[1]);
                        if (namePerson.length == 3) {
                            person.setFatherName(namePerson[2]);
                        }
                    }
                    //Заполняем адрес
                    if (cell.getColumnIndex() == 2) {
                        cell.setCellType(1);
                        address.setCity(cell.getStringCellValue().trim());
                    }
                    if (cell.getColumnIndex() == 3) {
                        cell.setCellType(1);
                        address.setStreet(cell.getStringCellValue().trim());
                    }
                    if (cell.getColumnIndex() == 4) {
                        cell.setCellType(1);
                        address.setHome(cell.getStringCellValue());
                    }
                    if (cell.getColumnIndex() == 5) {
                        cell.setCellType(1);
                        address.setApartment(cell.getStringCellValue().trim());
                    }
                    //Заполняем телефон
                    if (cell.getColumnIndex() == 6) {
                        cell.setCellType(1);
                        person.setTelephone(cell.getStringCellValue().trim());
                    }
                    //Заполняем льготы
                    if (cell.getColumnIndex() == 7) {
                        cell.setCellType(1);
                        person.setBenefits(cell.getStringCellValue().trim());
                    }
                }
                person.setAddress(address);
                //Проверяем существует ли гараж в базе
                //true будет когда гаража не существует после чего происходит инверсия
                if (!garagService.existGarag(garag)) {
                    //Если существует идем дальше
                    continue;
                } else {
                    //Если не существует находим владельца
                    Person findPerson = personService.getByFio(person);
                    //Назначаем владельца
                    if (findPerson != null) {
                        //если влыделец уже существовал
                        garag.setPerson(findPerson);
                    } else {
                        //если впервые
                        garag.setPerson(person);
                    }
                    if (garag.getPerson().getLastName() == null) {
                        continue;
                    } else {
                        //Сохраняем в базу
                        garagService.saveOrUpdate(garag);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Парсер льготников
    @Transactional
    public void parsePerson(MultipartFile file) {
        try {
            InputStream in = null;
            HSSFWorkbook wb = null;
            try {
                in = file.getInputStream();
                wb = new HSSFWorkbook(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Sheet sheet = wb.getSheetAt(0);
            Iterator<Row> it = sheet.iterator();
            while (it.hasNext()) {
                Person person = new Person();
                Row row = it.next();
                Iterator<Cell> cells = row.iterator();
                while (cells.hasNext()) {
                    Cell cell = cells.next();
                    if (cell.getColumnIndex() == 0) {
                        cell.setCellType(1);
                        String[] namePerson = cell.getStringCellValue().trim().split(" ");
                        person.setLastName(namePerson[0]);
                        person.setName(namePerson[1]);
                        if (namePerson.length == 3) {
                            person.setFatherName(namePerson[2]);
                        } else {
                            person.setFatherName("");
                        }
                    }
                    if (cell.getColumnIndex() == 1) {
                        cell.setCellType(1);
                        person.setBenefits(cell.getStringCellValue().trim());
                    }
                }
                Person newPerson = personService.getByFio(person);
                if (newPerson != null) {
                    newPerson.setBenefits(person.getBenefits());
                    personService.saveOrUpdate(newPerson);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


