package com.example.demo.controller;

import com.example.demo.model.SinhVien;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SinhVienController {

    @GetMapping(value = "/sinhvien", produces = {"application/json", "application/xml"})
    public List<SinhVien> getSinhVien() {
        return List.of(
                new SinhVien("SV01", "Nguyen Van A", 8.5),
                new SinhVien("SV02", "Tran Thi B", 7.9),
                new SinhVien("SV03", "Le Van C", 9.2)
        );
    }
}