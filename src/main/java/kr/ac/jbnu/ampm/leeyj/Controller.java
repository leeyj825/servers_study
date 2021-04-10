package kr.ac.jbnu.ampm.leeyj;


import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;

@RestController
public class Controller {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> responseEntity(HttpServletRequest request){
        ResponseEntity<?> responseEntity = null;

        responseEntity = new ResponseEntity<>("OK", HttpStatus.OK);

        Logger logger = LoggerFactory.getLogger("kr.ac.jbnu.ampm.leeyj");
        logger.info("log");
        return responseEntity;
    }
}
