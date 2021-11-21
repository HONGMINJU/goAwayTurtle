package com.turtle.goAwayTurtle.controller;

import com.turtle.goAwayTurtle.PredictAPI.PredictAPIImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@Controller
public class mainController {

    PredictAPIImpl predictAPI = new PredictAPIImpl();

    @PostMapping("/detectImg")
    @ResponseBody
    public long nameReturn(@RequestParam("file") MultipartFile file) throws IOException {
        long result = predictAPI.getPredict(file);
        System.out.println("결과 (1: 바른자세, 0: 바른자세X) at " + LocalDate.now() + " " + result);
        return result;
    }
}
