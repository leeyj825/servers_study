package kr.ac.jbnu.ampm.leeyj;


import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.security.Key;
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

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
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
            responseEntity = new ResponseEntity<>("BAD_REQUEST", HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }

    @RequestMapping(value = "/{id}/{field}", method = RequestMethod.GET)
    //id 조회 (DB.containsKey(id)) -> 내부 field 조회 (for each 문 : keySet 비교) -> 있으면 field 데이터 리턴, 없으면 NOT_FOUND
    @ResponseBody
    public ResponseEntity<?> getFieldResponseEntity(HttpServletRequest request, @PathVariable String id, @PathVariable String field){ //field가 Key 타입은 불가능?
        ResponseEntity<?> responseEntity = null;

        if(!testDBHashMap.isEmpty()){ //비어있지 않을때
            if(id != null && !id.equals("") && !field.equals("") && testDBHashMap.containsKey(id)){
                for(Map<String, Object> tmp : testDBHashMap.get(id)) { //DB의 MAP.
                    for(String keys : tmp.keySet()) {
                        if (keys.equals(field)) { //키 값이 있으면.
                            responseEntity = new ResponseEntity<>(tmp.get(field), HttpStatus.OK);
                        }
                    }
                }
            }
            else{
                responseEntity = new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
            }
        }
        else{ //DB가 비어있을 때.
            responseEntity = new ResponseEntity<>("BAD_REQUEST", HttpStatus.BAD_REQUEST);
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
            responseEntity = new ResponseEntity<>("BAD_REQUEST", HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }

    @RequestMapping(value = "/post/{id}/{field}", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> postFieldResponseEntity(HttpServletRequest request, @PathVariable String id, @RequestBody Map<String, Object> requestMap, @PathVariable String field){
        ResponseEntity<?> responseEntity = null;
        ArrayList<Map<String, Object>> postValueArrayList = null;
        Map<String, Object> desc = null;

        if (id != null && !id.equals("") && field!=null && !field.equals("")) {
            if (!testDBHashMap.isEmpty() && testDBHashMap.containsKey(id)) { //id가 존재하는 경우.
                postValueArrayList = testDBHashMap.get(id);

                if (field.equals("desc")) {
                    for (Map<String, Object> tmp : postValueArrayList) {
                        for (String keys : tmp.keySet()) {
                            if (keys.equals("description")) {

                                Map<String, Object> map = (Map<String, Object>) tmp.get("description");

                                desc = new HashMap<String, Object>();
                                desc.put("index", Integer.toString(map.size() + 1));
                                desc.put("value", (String) requestMap.get("description"));
//
                                map.put("desc"+(map.size() + 1), desc);
                                requestMap.put("description", map);
                                responseEntity = new ResponseEntity<>(requestMap, HttpStatus.OK);
                            }
                        }
                    }
                }
                else if (field.equals("music")) {
                    for (Map<String, Object> tmp : postValueArrayList) {
                        for (String keys : tmp.keySet()) {
                            if (keys.equals(field)) {
                                Map<String, Object> map = (Map<String, Object>) tmp.get(field);
                                String s = (String) requestMap.get("m"); //key 값 수정 위해 임시저장.
                                requestMap.remove("m"); //삭제
                                requestMap.put("m" + (map.size() + 1), s); //key 값 수정하여 다시 put
                                map.put("m" + (map.size() + 1), s); //music list에 추가.
                                responseEntity = new ResponseEntity<>(requestMap, HttpStatus.OK);
                            }
                        }
                    }
                }

            }
            else { //id가 존재하지 않는 경우.
                responseEntity = new ResponseEntity<>("BAD_REQUEST", HttpStatus.BAD_REQUEST);
            }
        }//id 또는 field를 잘못 입력한 경우.
        else {
            responseEntity = new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
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
                }
            }
            else { //널 값이나 공백을 입력했을 때. id가 존재하지 않을 때.
                responseEntity = new ResponseEntity<>("BAD_REQUEST", HttpStatus.BAD_REQUEST);
            }
        }

        else{ //DB가 비어있는 경우.
            responseEntity = new ResponseEntity<>("BAD_REQUEST", HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }

}