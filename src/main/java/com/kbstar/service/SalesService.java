package com.kbstar.service;

import com.kbstar.dto.Sales;
import com.kbstar.frame.KBService;
import com.kbstar.mapper.SalesMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SalesService implements KBService<String, Sales> {
    @Autowired
    SalesMapper mapper;


    @Override
    public void register(Sales sales) throws Exception {
        mapper.insert(sales);
    }

    @Override
    public void remove(String s) throws Exception {

    }

    @Override
    public void modify(Sales sales) throws Exception {

    }

    @Override
    public Sales get(String s) throws Exception {
        return null;
    }

    @Override
    public List<Sales> get() throws Exception {
        return mapper.selectall();
    }
    public List<Sales> getdate() throws Exception {
        return mapper.getdate();
    }
    public List<Sales> getmale() throws Exception {
        return mapper.getmale();
    }
    public List<Sales> getfemale() throws Exception {
        return mapper.getfemale();
    }
}
