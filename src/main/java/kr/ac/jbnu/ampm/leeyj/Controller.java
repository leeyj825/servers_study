package kr.ac.jbnu.ampm.leeyj;


import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import sun.awt.image.ImageWatched;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class Controller {
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> responseEntity(HttpServletRequest request, @PathVariable String id){
        ResponseEntity<?> responseEntity = null;

        Map<String, Object> voMap = null;

        if (id != null && !id.equals("")) {
            voMap = new HashMap<String, Object>();

            voMap.put("books", new LinkedHashMap<String, Object>() {{ //linked hashmap : 순서 보장.

                put("book3", new HashMap<String , Object>(){{
                    put("name", "디지털공학개론");
                }}); //1

            put("book2", "소프트웨어공학개론");
            put("book1", "마션");
            }}); //2

            voMap.put("name", "이유정"); //3
            voMap.put("age", 22); //4

            responseEntity = new ResponseEntity<>(voMap, HttpStatus.OK);
        }
        else{

            responseEntity = new ResponseEntity<>("not found", HttpStatus.NOT_FOUND);
        }

        return responseEntity;
    }
}

/*
{
	{
	"books" : {
	  "book3" : {
			"name" : "디지털공학개론"
	  },
	  "book2" : "소프트웨어공학개론"
	  "book1" : "마션"
	},
	"name": "이유정"
    "age" : "22"
}
}
 */



















