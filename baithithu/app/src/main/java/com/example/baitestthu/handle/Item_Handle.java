package com.example.baitestthu.handle;

import com.example.baitestthu.model.Student;

public interface Item_Handle {
    public void Delete(String id) ;
    public void Update(String id, Student student);
}
