package com.example.smsSpringTest.service;

import com.example.smsSpringTest.model.Salary;
import com.example.smsSpringTest.model.response.SalaryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * author : 신기훈
 * date : 2024-11-19
 * comment : 급여 계산용 Service
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SalaryService {

    // 월급 계산할 때 약 한 달
    public static final double M_CAL = 4.345238336713996;

    // 연봉 계산할 때 약 한 달
    public static final double Y_CAL = 4.345238125422583;

    public static final double tax9 = 1 - 0.094 ;  // 0.906 , 세금 9.4 % 일때
    public static final double tax3 = 1 - 0.033 ; // 0.967 , 세금 3.3 % 일때

    // 급여 계산기
    public SalaryResponse salaryCalculation(Salary salary) throws Exception {
        SalaryResponse salaryResponse = new SalaryResponse();

        String type1 = salary.getType1();
        String type2 = salary.getType2();
        String tax = salary.getTax();
        double hourSalary = salary.getHourSalary(); // 시급
        double workTime = salary.getWorkTime(); // 일일 근무시간 (30분 단위는 .5로)
        double weekWorkDay = salary.getWeekWorkDay(); // 일주 근무일수

        double dailySalary; // 일급
        double weekSalary; // 주급
        double monthSalary; // 월급
        double yearSalary; // 연봉
        double weekHolidayPay; // 주휴 수당
        double totalSalary = 0; // 최종 급여 금액

        try {
            if (type1.equals("시급")) {

                if (type2.equals("일급")) {
                    // 일급 계산
                    dailySalary = Math.round(hourSalary * workTime);
                    if ("9.4%".equals(tax)) {
                        dailySalary *= tax9;
                    } else if ("3.3%".equals(tax)) {
                        dailySalary *= tax3;
                    }
                    Math.round(totalSalary = dailySalary);
                    salaryResponse.setDailySalary((int) dailySalary);
                } else if (type2.equals("주급")) {
                    // 주급 계산
                    weekSalary = Math.round(hourSalary * workTime * weekWorkDay); // 예상 주급

                    weekHolidayPay = weekHolidayPayCal(workTime, weekWorkDay, hourSalary); // 예상 주휴수당
                    if("9.4%".equals(tax)){
                        weekSalary *= tax9;
                        weekHolidayPay *= tax9;
                    } else if("3.3%".equals(tax)){
                        weekSalary *= tax3;
                        weekHolidayPay *= tax3;
                    }
                    totalSalary = Math.round(weekSalary + weekHolidayPay);
                    salaryResponse.setWeekSalary((int) weekSalary);
                    salaryResponse.setWeekHolidayPay((int) weekHolidayPay);
                } else if (type2.equals("월급")) {
                    // 월급 계산
                    weekSalary = Math.round(hourSalary * workTime * weekWorkDay); // 예상 주급
                    weekHolidayPay = weekHolidayPayCal(workTime, weekWorkDay, hourSalary); // 예상 주휴수당

                    log.info(" 예상 주급 = " + weekSalary);
                    log.info(" 예상 주휴수당 = " + weekHolidayPay);
                    monthSalary = Math.round(weekSalary * M_CAL); // 예상 월급
                    log.info(" 예상 월급 = " + monthSalary);
                    weekHolidayPay = Math.round(weekHolidayPay * M_CAL); // 예상 주휴수당

                    log.info(" 예상 주휴수당2 = " + weekHolidayPay);
                    if("9.4%".equals(tax)){
                        monthSalary *= tax9;
                        weekHolidayPay *= tax9;
                    } else if("3.3%".equals(tax)){
                        monthSalary *= tax3;
                        weekHolidayPay *= tax3;
                    }

                    totalSalary = Math.round(monthSalary + weekHolidayPay);
                    salaryResponse.setMonthSalary((int) monthSalary);
                    salaryResponse.setWeekHolidayPay((int) weekHolidayPay);
                } else if (type2.equals("연봉")) {
                    // 연봉 계산
                    weekSalary = Math.round(hourSalary * workTime * weekWorkDay); // 예상 주급
                    weekHolidayPay = weekHolidayPayCal(workTime, weekWorkDay, hourSalary); // 예상 주휴수당

                    log.info(" 예상 주급 = " + weekSalary);
                    log.info(" 예상 주휴수당 = " + weekHolidayPay);
                    yearSalary = Math.round(weekSalary * Y_CAL * 12); // 예상 월급
                    log.info(" 예상 연봉 = " + yearSalary);
                    weekHolidayPay = Math.round(weekHolidayPay * Y_CAL * 12); // 예상 주휴수당

                    log.info(" 예상 주휴수당2 = " + weekHolidayPay);

                    if("9.4%".equals(tax)){
                        yearSalary *= tax9;
                        weekHolidayPay *= tax9;
                    } else if("3.3%".equals(tax)){
                        yearSalary *= tax3;
                        weekHolidayPay *= tax3;
                    }
                    totalSalary = Math.round(yearSalary + weekHolidayPay);
                    log.info(" 최종 환산금액 = " + totalSalary);
                    salaryResponse.setYearSalary((int) yearSalary);
                    salaryResponse.setWeekHolidayPay((int) weekHolidayPay);
                }

                // type1 = 시급 끝
            } else if(type1.equals("일급")) {
                dailySalary = salary.getDailySalary(); // 일급을 클라이언트로부터 받음
                hourSalary = Math.round(dailySalary / workTime);  // 주휴수당 계산할때도 사용하기 위함
                weekSalary = Math.round(dailySalary * weekWorkDay); // 예상 주급
                weekHolidayPay = weekHolidayPayCal(workTime, weekWorkDay, hourSalary); // 예상 주휴수당

                if(type2.equals("시급")){
                    // 일급으로 시급 구하기
                    if("9.4%".equals(tax)){
                        hourSalary *= tax9;
                    } else if("3.3%".equals(tax)){
                        hourSalary *= tax3;
                    }
                    totalSalary = Math.round(hourSalary);
                    salaryResponse.setHourSalary( (int) hourSalary);
                } else if(type2.equals("주급")){
                    // 일급으로 주급 구하기
//                    weekSalary = Math.round(dailySalary * weekWorkDay); // 예상 주급
//                    weekHolidayPay = weekHolidayPayCal(workTime, weekWorkDay, hourSalary); // 예상 주휴수당

                    if("9.4%".equals(tax)){
                        weekSalary *= tax9;
                        weekHolidayPay *= tax9;
                    } else if("3.3%".equals(tax)){
                        weekSalary *= tax3;
                        weekHolidayPay *= tax3;
                    }
                    totalSalary = Math.round(weekSalary + weekHolidayPay);
                    salaryResponse.setWeekSalary((int) weekSalary);
                    salaryResponse.setWeekHolidayPay((int) weekHolidayPay);
                } else if(type2.equals("월급")){
                    monthSalary = Math.round(weekSalary * M_CAL); // 예상 월급
                    log.info(" 예상 월급 = " + monthSalary);
                    weekHolidayPay = Math.round(weekHolidayPay * M_CAL); // 예상 주휴수당
                    log.info(" 예상 주휴수당2 = " + weekHolidayPay);
                    if("9.4%".equals(tax)){
                        monthSalary *= tax9;
                        weekHolidayPay *= tax9;
                    } else if("3.3%".equals(tax)){
                        monthSalary *= tax3;
                        weekHolidayPay *= tax3;
                    }
                    totalSalary = Math.round(monthSalary + weekHolidayPay);
                    salaryResponse.setMonthSalary((int) monthSalary);
                    salaryResponse.setWeekHolidayPay((int) weekHolidayPay);
                } else if(type2.equals("연봉")){
                    yearSalary = Math.round(weekSalary * Y_CAL * 12); // 예상 연봉
                    log.info(" 예상 연봉 = " + yearSalary);
                    weekHolidayPay = Math.round(weekHolidayPay * Y_CAL *12); // 예상 주휴수당

                    log.info(" 예상 주휴수당2 = " + weekHolidayPay);
                    if("9.4%".equals(tax)){
                        yearSalary *= tax9;
                        weekHolidayPay *= tax9;
                    } else if("3.3%".equals(tax)){
                        yearSalary *= tax3;
                        weekHolidayPay *= tax3;
                    }
                    totalSalary = Math.round(yearSalary + weekHolidayPay);
                    salaryResponse.setYearSalary((int) yearSalary);
                    salaryResponse.setWeekHolidayPay((int) weekHolidayPay);
                }
                // type1이 일급일때 계산 끝

            } else if(type1.equals("주급")){
                weekSalary = salary.getWeekSalary();

                if(type2.equals("시급")) {
                    hourSalary = Math.round(weekSalary / workTime / weekWorkDay);
                    if("9.4%".equals(tax)){
                        hourSalary *= tax9;
                    } else if("3.3%".equals(tax)){
                        hourSalary *= tax3;
                    }
                    totalSalary = Math.round(hourSalary);
                    salaryResponse.setHourSalary((int) hourSalary);
                } else if(type2.equals("일급")){
                    dailySalary = Math.round(weekSalary / weekWorkDay);
                    if("9.4%".equals(tax)){
                        dailySalary *= tax9;
                    } else if("3.3%".equals(tax)){
                        dailySalary *= tax3;
                    }
                    totalSalary = Math.round(dailySalary);
                    salaryResponse.setDailySalary((int) dailySalary);
                } else if(type2.equals("월급")) {
                    monthSalary = Math.round(weekSalary * M_CAL); // 예상 월급
                    if("9.4%".equals(tax)){
                        monthSalary *= tax9;
                    } else if("3.3%".equals(tax)){
                        monthSalary *= tax3;
                    }
                    totalSalary = Math.round(monthSalary);
                    salaryResponse.setMonthSalary((int) monthSalary);
                } else if(type2.equals("연봉")) {
                    yearSalary = Math.round(weekSalary * Y_CAL * 12); // 예상 연봉
                    if("9.4%".equals(tax)){
                        yearSalary *= tax9;
                    } else if("3.3%".equals(tax)){
                        yearSalary *= tax3;
                    }
                    totalSalary = Math.round(yearSalary);
                    salaryResponse.setYearSalary((int) yearSalary);
                }
                // type1이 주급일때 계산 끝

            } else if(type1.equals("월급")){
                monthSalary = salary.getMonthSalary();

                if(type2.equals("시급")) {
                    hourSalary = Math.round(monthSalary / workTime / weekWorkDay / M_CAL);
                    if("9.4%".equals(tax)){
                        hourSalary *= tax9;
                    } else if("3.3%".equals(tax)){
                        hourSalary *= tax3;
                    }
                    totalSalary = Math.round(hourSalary);
                    salaryResponse.setHourSalary((int) hourSalary);
                } else if(type2.equals("일급")) {
                    monthSalary = Math.round(monthSalary / weekWorkDay / M_CAL);
                    if("9.4%".equals(tax)){
                        monthSalary *= tax9;
                    } else if("3.3%".equals(tax)){
                        monthSalary *= tax3;
                    }
                    totalSalary = Math.round(monthSalary);
                    salaryResponse.setMonthSalary((int) monthSalary);
                } else if(type2.equals("주급")) {
                    weekSalary = Math.round(monthSalary / M_CAL);
                    if("9.4%".equals(tax)){
                        weekSalary *= tax9;
                    } else if("3.3%".equals(tax)){
                        weekSalary *= tax3;
                    }
                    totalSalary = Math.round(weekSalary);
                    salaryResponse.setWeekSalary((int) weekSalary);
                } else if(type2.equals("연봉")) {
                    yearSalary = Math.round(monthSalary * 12);
                    if("9.4%".equals(tax)){
                        yearSalary *= tax9;
                    } else if("3.3%".equals(tax)){
                        yearSalary *= tax3;
                    }
                    totalSalary = Math.round(yearSalary);
                    salaryResponse.setYearSalary((int) yearSalary);
                }
                // type1 = 월급 계산 끝

            } else if(type1.equals("연봉")){
                yearSalary = salary.getYearSalary();

                if(type2.equals("시급")) {
                    hourSalary = Math.round(yearSalary / workTime / weekWorkDay / Y_CAL / 12);
                    if("9.4%".equals(tax)){
                        hourSalary *= tax9;
                    } else if("3.3%".equals(tax)){
                        hourSalary *= tax3;
                    }
                    totalSalary = Math.round(hourSalary);
                    salaryResponse.setHourSalary((int) hourSalary);
                } else if(type2.equals("일급")) {
                    dailySalary = Math.round(yearSalary / weekWorkDay / Y_CAL / 12);
                    if("9.4%".equals(tax)){
                        dailySalary *= tax9;
                    } else if("3.3%".equals(tax)){
                        dailySalary *= tax3;
                    }
                    totalSalary = Math.round(dailySalary);
                    salaryResponse.setDailySalary((int) dailySalary);
                } else if(type2.equals("주급")){
                    weekSalary = Math.round(yearSalary / Y_CAL / 12);
                    if("9.4%".equals(tax)){
                        weekSalary *= tax9;
                    } else if("3.3%".equals(tax)){
                        weekSalary *= tax3;
                    }
                    totalSalary = Math.round(weekSalary);
                    salaryResponse.setWeekSalary((int) weekSalary);
                } else if(type2.equals("월급")){
                    monthSalary = Math.round(yearSalary / 12);
                    if("9.4%".equals(tax)){
                        monthSalary *= tax9;
                    } else if("3.3%".equals(tax)){
                        monthSalary *= tax3;
                    }
                    totalSalary = Math.round(monthSalary);
                    salaryResponse.setMonthSalary((int) monthSalary);
                }
            }

            salaryResponse.setTotalSalary((int) totalSalary);
            salaryResponse.setCode("C000");
            salaryResponse.setMessage(" 급여 계산 성공 ");
        } catch (Exception e) {
            salaryResponse.setCode("E001");
            salaryResponse.setMessage(" Error !!! ");
            log.info(e.getMessage());
        }
        return salaryResponse;
    }

    // 주휴수당 기준 조건 최소 15시간 이상 최대 40시간까지만
    private double minAndMaxChk(double workTime, double weekWorkDay){
        //  일주 근무시간  =  일일 근무시간 * 일주 근무일수
        double weekWorkTime = workTime * weekWorkDay;

        if(weekWorkTime >= 40) {
            weekWorkTime = 40;
        }
        if(weekWorkTime < 15){
            weekWorkTime = 0;
        }
        log.info("일주일 총 근무 시간 = " +  weekWorkTime);
        return weekWorkTime;
    }

    // 주휴수당 (1주일치) 계산기
    private double weekHolidayPayCal(double workTime, double weekWorkDay, double hourSalary) {

        // 1주일 총 일한시간
        double weekWorkTime = minAndMaxChk(workTime, weekWorkDay);

        double weekHolidayPay = Math.round((weekWorkTime / 40) * 8 * hourSalary);

        log.info("일주일치 주휴수당 = " + weekHolidayPay);

        return weekHolidayPay;
    }

//    // 월급 계산기
//    private double monthSalaryCal(double weekSalary) {
//
//
//
//        return 0;
//    }

}
