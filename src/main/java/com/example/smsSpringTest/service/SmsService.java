package com.example.smsSpringTest.service;

import com.example.smsSpringTest.mapper.SmsMapper;
import com.example.smsSpringTest.model.SmsForm;
import com.example.smsSpringTest.model.response.SmsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;

/**
 * author : 신기훈
 * date : 2024-09-20
 * comment : sms 전송 Service
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class SmsService {
    private final static String apiUrl        =    "https://sslsms.cafe24.com/sms_sender.php";

    private final static String userAgent    =    "Mozilla/5.0";

    private final static String charset        =    "UTF-8";

//    private final static boolean isTest        =    true; //
    private final static boolean isTest        =    false;

    private final SmsMapper smsMapper;

    // sms 유저 id
    @Value("${sms.userId}")
    private String smsId;

    // 인증키
    @Value("${sms.secureKey}")
    private String secureKey;


    //private static final Logger logger =    Logger.getLogger(smsService.class);


    public SmsResponse sendSms(SmsForm smsForm) throws IOException {


        SmsResponse smsResponse = new SmsResponse();
        log.info("서비스 smsForm = " + smsForm);
        log.info("서비스 try 전 호출 됨");
        try{


            log.info("try 후 호출 됨");
            URL obj = new URL(apiUrl);

            HttpsURLConnection con= (HttpsURLConnection) obj.openConnection();

//            con.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            con.setRequestProperty("Content-type", "application/json"); //json으로 받기위해서

            con.setRequestProperty("Accept-Charset", charset);

            con.setRequestMethod("POST");

            con.setRequestProperty("User-Agent", userAgent);

            log.info("try 내부 = 수신번호 : " + smsForm.getRPhone());


            String postParams = "user_id="+base64Encode(smsId)

                    +"&secure="+base64Encode(secureKey)

//                    +"&msg="+base64Encode(smsForm.getMsg())+"&sphone="+base64Encode(smsForm.getSphone())
                    +"&msg="+base64Encode(smsForm.getMsg())+"&sPhone1="+base64Encode(smsForm.getSPhone1())+"&sPhone2="+base64Encode(smsForm.getSPhone2())+"&sPhone3="+base64Encode(smsForm.getSPhone3())

                    +"&mode="+base64Encode("1")+"&smsType="+base64Encode(smsForm.getSmsType())+"&rDate"+base64Encode(smsForm.getRDate())+"&rTime"+base64Encode(smsForm.getRTime()); // SMS/LMS 여부

            //test 모드일 경우 실제로 SMS발송은 되지 않고 성공적인 호출 확인 여부만 확인 할 수 있도록 함

//            log.info("포스트파람 = " + postParams);
//           String postParams = new Gson().toJson(smsRequest);
            log.info("포스트파람 = " + postParams);

            if(isTest) {

//                postParams+="&testflag"+base64Encode("Y");

            }


            //For POST only    - START

            con.setDoOutput(true);

            OutputStream os = con.getOutputStream();

            os.write(postParams.getBytes());

            os.flush();

            os.close();

            //For POST only - END

            int responseCode = con.getResponseCode();

            //logger.info("POST Response Code::"+responseCode);



            if(responseCode == HttpURLConnection.HTTP_OK){ // success

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String inputLine;

                StringBuffer buf  = new StringBuffer();



                while((inputLine=in.readLine())!=null){
                    log.info("Response Line: " + inputLine);
                    buf.append(inputLine);

                }

                in.close();


                log.info("문자 테스트 1차 성공");


                //logger.info("SMS Content : "+buf.toString());
                String origin_sms_url = "https://sslsms.cafe24.com/sms_sender.php"; // 인코딩 전 cafe24 제공 url
                String sms_url = base64Encode("https://sslsms.cafe24.com/sms_sender.php"); // SMS 전송요청 URL
                String user_id = base64Encode(smsId); // SMS아이디
                String secure = base64Encode(secureKey);//인증키
                String msg = base64Encode(smsForm.getMsg());
                String rPhone = base64Encode(smsForm.getRPhone());
                String sPhone1 = base64Encode(smsForm.getSPhone1());
                String sPhone2 = base64Encode(smsForm.getSPhone2());
                String sPhone3 = base64Encode(smsForm.getSPhone3());
                String rDate = base64Encode(smsForm.getRDate());
                String rTime = base64Encode(smsForm.getRTime());
                String mode = base64Encode("1");
                String subject = "";


                if(smsForm.getSmsType().equals("L")) {
                    subject = base64Encode(smsForm.getSubject());
                }
                String testFlag = base64Encode(smsForm.getTestFlag());
                String destination = base64Encode("");
                String repeatFlag = base64Encode("");
                String repeatNum = base64Encode("");
                String repeatTime = base64Encode("");
                String returnurl = base64Encode("");
                String nointeractive = base64Encode("");
                String smsType = base64Encode(smsForm.getSmsType());

                log.info("origin_sms_url = " + origin_sms_url);
                log.info("SMSurl = " + sms_url);


                String[] host_info = origin_sms_url.split("/");

                log.info("host_info = " + host_info);

                String host = host_info[2];

                log.info("host = " + host);

                String path = "/" + host_info[3];
                int port = 80;
                
                String charsetType = "UTF-8";

                // 데이터 맵핑 변수 정의
                String arrKey[]
                        = new String[] {"user_id","secure","msg", "rphone","sphone1","sphone2","sphone3","rdate","rtime"
                        ,"mode","testflag","destination","repeatFlag","repeatNum", "repeatTime", "smsType", "subject"};
                String valKey[]= new String[arrKey.length] ;
                valKey[0] = user_id;
                valKey[1] = secure;
                valKey[2] = msg;
                valKey[3] = rPhone;
                valKey[4] = sPhone1;
                valKey[5] = sPhone2;
                valKey[6] = sPhone3;
                valKey[7] = rDate;
                valKey[8] = rTime;
                valKey[9] = mode;
                valKey[10] = testFlag;
                valKey[11] = destination;
                valKey[12] = repeatFlag;
                valKey[13] = repeatNum;
                valKey[14] = repeatTime;
                valKey[15] = smsType;
                valKey[16] = subject;

                String boundary = "";
                Random rnd = new Random();
                String rndKey = Integer.toString(rnd.nextInt(32000));
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] bytData = rndKey.getBytes();
                md.update(bytData);
                byte[] digest = md.digest();
                for(int i =0;i<digest.length;i++)
                {
                    boundary = boundary + Integer.toHexString(digest[i] & 0xFF);
                }
                boundary = "---------------------"+boundary.substring(0,11);

                // 본문 생성
                String data = "";
                String index = "";
                String value = "";
                for (int i=0;i<arrKey.length; i++)
                {
                    index =  arrKey[i];
                    value = valKey[i];
                    data +="--"+boundary+"\r\n";
                    data += "Content-Disposition: form-data; name=\""+index+"\"\r\n";
                    data += "\r\n"+value+"\r\n";
                    data +="--"+boundary+"\r\n";
                }

                //out.println(data);

                log.info("data = " + data);

                InetAddress addr = InetAddress.getByName(host);
                Socket socket = new Socket(host, port);
                // 헤더 전송
                BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), charsetType));
                wr.write("POST "+path+" HTTP/1.0\r\n");
                wr.write("Content-Length: "+data.length()+"\r\n");
                wr.write("Content-type: multipart/form-data, boundary="+boundary+"\r\n");
                wr.write("\r\n");

                // 데이터 전송
                wr.write(data);
                wr.flush();

                // 결과값 얻기
                BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream(),charsetType));
                String line;
                String alert = "";
                ArrayList tmpArr = new ArrayList();
                while ((line = rd.readLine()) != null) {
                    tmpArr.add(line);
                }
                wr.close();
                rd.close();

                String tmpMsg = (String)tmpArr.get(tmpArr.size()-1);
                String[] rMsg = tmpMsg.split(",");
                String Result= rMsg[0]; //발송결과

                String Count= ""; //잔여건수
                if(rMsg.length>1) {Count= rMsg[1]; }

                log.info("tmpMsg = " + tmpMsg);
                log.info("rMsg = " + rMsg);

                log.info("Result = " + Result);
                log.info("잔여 문자 개수 : Count = " + Count);

                if(Result.equals("success")){
                    smsResponse.setSmsForm(smsForm);
                    smsResponse.setCode("C000");
                    smsResponse.setMessage("문자 전송 성공 !! 남은 잔여 문자 " + Count + "건 남았습니다.");
//                    return "문자 전송 성공!! 남은 잔여 문자 " + Count; // 성공시 -> 잔여 문자 개수 반환

                    //------ db에 저장할 sPhone, rPhone, smsType, msg
                    String originSphone = smsForm.getSPhone1() +"-" +smsForm.getSPhone2()+"-"+smsForm.getSPhone3();
                    String originRphone = smsForm.getRPhone();
                    String originSmsType = smsForm.getSmsType();
                    String originMsg = smsForm.getMsg();

                    smsForm.setSPhone(originSphone);
                    smsForm.setRPhone(originRphone);
                    smsForm.setSmsType(originSmsType);
                    smsForm.setMsg(originMsg);

                    int addMsg = smsMapper.addMsg(smsForm);
                    if(addMsg == 1){
                        log.info("db에 문자 저장 성공");
                    } else {
                        log.info("db에 문자 저장 실패 !!!");
                    }
                    // --------------------------------------------
                } else{
//                    return Result; // 실패시
                    smsResponse.setCode("E002");
                    smsResponse.setMessage(Result);
                }
            }else{

                //logger.error("POST request not worked");
                log.info("문자 테스트 실패");
//                return "실패";
                smsResponse.setCode("E001");
                smsResponse.setMessage("오류가 발생하였습니다.");
            }

        }catch(IOException ex){

            log.info("SMS IOException:"+ex.getMessage());

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
//        return "실패";
          return smsResponse;
    }

    public static String base64Encode(String str)  throws java.io.IOException {

        if (str == null) {
            return "";  // null이면 빈 문자열 반환
        }
        //sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] strByte = str.getBytes(StandardCharsets.UTF_8);
        String result = encoder.encodeToString(strByte);
        return result ;
    }

    /**
     * BASE64 Decoder
     * @param str
     * @return
     */

    public static String base64Decode(String str)  throws java.io.IOException {

       // sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();

        if (str == null) {
            return "";  // null이면 빈 문자열 반환
        }

        Base64.Decoder decoder = Base64.getDecoder();
        byte[] strByte = decoder.decode(str);
        String result = new String(strByte);
        return result ;
    }

}
