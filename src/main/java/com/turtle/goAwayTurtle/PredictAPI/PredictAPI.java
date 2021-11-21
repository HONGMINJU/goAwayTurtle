package com.turtle.goAwayTurtle.PredictAPI;


import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PredictAPI {
    public long getPredict(MultipartFile multipartFile) throws IOException;
}
