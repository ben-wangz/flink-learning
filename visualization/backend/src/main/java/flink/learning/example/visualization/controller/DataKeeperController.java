package flink.learning.example.visualization.controller;

import flink.learning.example.visualization.core.Response;
import flink.learning.example.visualization.service.DataKeeperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DataKeeperController {
    @Autowired
    private DataKeeperService dataKeeperService;

    @RequestMapping("/updateData")
    public Response updateData(
            @RequestParam("name") String name,
            @RequestParam("json") String json
    ) {
        dataKeeperService.updateData(name, json);
        return Response.Builder.newInstance()
                .success(true)
                .build();
    }

    @RequestMapping("/queryDataWithPrefix")
    public Response queryDataWithPrefix(
            @RequestParam("namePrefix") String namePrefix
    ) {
        Map<String, String> data = dataKeeperService.queryDataWithPrefix(namePrefix);
        return Response.Builder.newInstance()
                .success(true)
                .data(data)
                .build();
    }
}
