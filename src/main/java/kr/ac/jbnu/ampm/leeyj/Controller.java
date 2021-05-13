package kr.ac.jbnu.ampm.leeyj;


import com.sun.applet2.AppletParameters;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import sun.awt.image.ImageWatched;

import javax.servlet.http.HttpServletRequest;
import javax.swing.*;
import java.util.*;

@RestController
public class Controller {

    private static  HashMap<String, ArrayList<Map<String, Object>>> testDBHashMap = new HashMap<String, ArrayList<Map<String, Object>>>();

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getAllResponseEntity(HttpServletRequest request){
        ResponseEntity<?> responseEntity = null;

        if(!testDBHashMap.isEmpty()){ //비어있지 않을때
            responseEntity = new ResponseEntity<>(testDBHashMap, HttpStatus.OK);
        }
        else{
            responseEntity = new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
        }

        return responseEntity;
    }

    @RequestMapping(value = "/{id]", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getResponseEntity(HttpServletRequest request, @PathVariable String id){
        ResponseEntity<?> responseEntity = null;

        if(!testDBHashMap.isEmpty()){ //비어있지 않을때
            if(id != null && !id.equals("") && testDBHashMap.containsKey(id)){
                responseEntity = new ResponseEntity<>(testDBHashMap.get(id), HttpStatus.OK);
            }
            else{
                responseEntity = new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
            }
        }
        else{
            responseEntity = new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
        }

        return responseEntity;
    }

    @RequestMapping(value = "/post/{id}", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> postResponseEntity(HttpServletRequest request, @PathVariable String id, @RequestBody Map<String, Object> requestMap){
        ResponseEntity<?> responseEntity = null;
        ArrayList<Map<String, Object>> postValueArrayList = null;  //데이터 받아서 임시로 두고 대체하는 용도

        if (id != null && !id.equals("")){
            if(testDBHashMap.containsKey(id)){ //키가 존재하는 경우.
                postValueArrayList = testDBHashMap.get(id);
            }
            else{ //동일한 키가 존재하지 않는 경우.
                postValueArrayList = new ArrayList<Map<String, Object>>();
            }

            postValueArrayList.add(requestMap);

            if(testDBHashMap.containsKey(id)){ //아이디가 존재하면 수정.
                testDBHashMap.replace(id, postValueArrayList);
            }
            else{ //존재하지 않으면 새로운 데이터로 추가.
                testDBHashMap.put(id, postValueArrayList);
            }

            responseEntity = new ResponseEntity<>(requestMap, HttpStatus.OK);
        }
        else{
            responseEntity = new ResponseEntity<>("NOT_CONTAIN", HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> deleteResponseEntity(HttpServletRequest request, @PathVariable String id){
        ResponseEntity<?> responseEntity = null;

        if(id!=null && !id.equals("")){
            if(testDBHashMap.containsKey(id)){ //키가 존재하는 경우.
                testDBHashMap.remove(id);
                responseEntity = new ResponseEntity<>("", HttpStatus.OK);
            }
            else{ //키가 없는 경우.
                responseEntity = new ResponseEntity<>("NOT_CONTAIN", HttpStatus.BAD_REQUEST);
            }
        }
        else{
            responseEntity = new ResponseEntity<>("BAD_REQUEST", HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }

    @RequestMapping(value = "/put/{id}", method = RequestMethod.PUT, produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> putResponseEntity(HttpServletRequest request, @PathVariable String id, @RequestBody Map<String, Object> requestMap) {
        ResponseEntity<?> responseEntity = null;
        ArrayList<Map<String, Object>> postValueArrayList = null;

        if (!testDBHashMap.isEmpty()) { //DB가 비어있지 않은 경우
            if (id != null && !id.equals("") && testDBHashMap.containsKey(id)) { //id를 입력했고 해당 id가 존재할때.
                for(Map<String, Object> tmp : testDBHashMap.get(id)){ //DB의 MAP
                   if(tmp.keySet().equals(requestMap.keySet())){ //키 값이 같으면
                       postValueArrayList = testDBHashMap.get(id); //null인 postValueArrayList에 id값의 MAP을 넣고
                       postValueArrayList.set(postValueArrayList.indexOf(tmp), requestMap); //값 변경//ArrayList.set : 특정 인덱스 값을 변경하는 메소드

                       testDBHashMap.replace(id, postValueArrayList); //값 수정.
                       responseEntity = new ResponseEntity<>(requestMap, HttpStatus.OK);
                   }
//                }
//
//                for (String newKey : requestMap.keySet()) {
//                    for (String i : testDBHashMap.keySet()) {
//                            for (String cmp : testDBHashMap.get(i).get(0).keySet()) { //DB의 MAP의 키값.
//                                if (newKey.equals((String) cmp)) {
//                                    postValueArrayList = (ArrayList<Map<String,Object>>)requestMap;
//                                    testDBHashMap.replace(id, postValueArrayList);
//                                    break;
//                                }
//                                break;
//                            }
//                        break;
//                    }
//                    break;
                }
            }
            else { //널 값이나 공백을 입력했을 때. id가 존재하지 않을 때.
                responseEntity = new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
            }
        }

        else{ //DB가 비어있는 경우.
            responseEntity = new ResponseEntity<>("BAD_REQUEST", HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }

}













































