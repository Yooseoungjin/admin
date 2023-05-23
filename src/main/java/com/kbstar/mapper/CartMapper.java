package com.kbstar.mapper;

import com.kbstar.dto.Cart;
import com.kbstar.frame.KBMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

//얘는 스프링 컨테이너 위에서 돌릴거다.
//JDBC에서는 class로 만들어서 고생했지만, 매퍼로 만들것. 그러기 위해 interface
//interface는 interface를 상속(extend)임.
@Repository
@Mapper
public interface CartMapper extends KBMapper<Integer, Cart> {
        public List<Cart> getmycart(String cid);
        public List<Cart> sumcart();
}
