package com.example.springdemo1221.entity;

import org.springframework.data.repository.CrudRepository;
import java.util.List;
public interface PersonDao extends CrudRepository<PersonEntity,Integer> {
    
    /** 
     * 
     * Description: 测试查询
     * @param id
     * @author huangweicheng
     * @date 2020/12/31
     * @return
    */ 
    List<PersonEntity> findById(int id);
    /** 
     * 
     * Description: 用户名和密码登录
     * @param username 用户名
     * @author huangweicheng
     * @date 2020/12/31
     * @return
    */ 
    PersonEntity findByLoginName(String username);
}
