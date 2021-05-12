package kr.ac.jbnu.ampm.leeyj;


import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import sun.awt.image.ImageWatched;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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
            if(id != null && id.equals("") && testDBHashMap.containsKey((id))){
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
            if(testDBHashMap.containsKey((id))){
                postValueArrayList = testDBHashMap.get(id);
            }
            else{
                postValueArrayList = new ArrayList<Map<String, Object>>();
            }

            postValueArrayList.add(requestMap);

            if(testDBHashMap.containsKey(id)){
                testDBHashMap.replace(id, postValueArrayList);
            }
            else{
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
            if(testDBHashMap.containsKey(id)){ //가지고 있을때만 작동해야함.
                testDBHashMap.remove(id);
                responseEntity = new ResponseEntity<>("", HttpStatus.OK); //빈 값으로 전달.
            }
            else{
                responseEntity = new ResponseEntity<>("NOT_FOUND", HttpStatus.BAD_REQUEST);
            }
        }
        else{
            responseEntity = new ResponseEntity<>("NOT_CONTAIN", HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }

}













































