package com.tjoeun.service;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.tjoeun.dao.GuestbookDAO;
import com.tjoeun.ibatis.MyAppSqlConfig;
import com.tjoeun.vo.GuestbookList;
import com.tjoeun.vo.GuestbookVO;
import com.tjoeun.vo.Param;

import java.sql.SQLException;
import java.util.HashMap;

public class SelectService {

    private static SelectService instance = new SelectService();

    private SelectService() {    }

    public static SelectService getInstance () {
        return instance;
    }

   /*  list.jsp에서 호출되는 브라우저에 표시할 페이지 번호(currentPage)를 넘겨받고 mapper를 얻어온 후
     GuestbookDAO 클래스의 1페이지 분량의 글 목록을 얻어오는 select sql 명령을 실행하는 메소드를 호출하는
     메소드    */
    public GuestbookList selectList(int currentPage) throws SQLException {
        System.out.println("SelectService 클래스의 selectService() 메소드 실행");
        SqlMapClient mapper = MyAppSqlConfig.getSqlMapInstance();

        // 1페이지 분량의 글 목록과 페이징 작업에 사용할 8개의 변수를 저장해서 리턴시킬 객체를 선언한다.
        GuestbookList guestbookList = null;
//        GuestbookDAO 클래스 객체에 2번 접근해서 sql 명령을 실행해야 하므로 GuestbookDAO 클래스의 객체를 미리 얻어둔다.
        GuestbookDAO dao = GuestbookDAO.getInstance();

        // 1페이지당 표시할 글의 개수를 정한다.
        int pageSize = 10;
        // 테이블에 저장된 전체 글의 개수를 얻어온다.
        int totalCount = dao.selectCount(mapper);
//        System.out.println(totalCount);

//        pageSize, totalCount, currentPage를 GuestbookList 클래스의 생성자로 넘겨서 1페이지 분량의 글 목록을 저장하고
//        페이징 작업에 사용할 8개의 변수를 초기화하는 GuestbookList 클래스 객체를 생성한다.
        guestbookList = new GuestbookList(pageSize , totalCount , currentPage);

//        parameterClass, resultClass는 데이터 타입을 반드시 1개만 적어야 한다.
//        xml 파일의 sql 명령으로 전달할 데이터가 2개 이상일 경우 데이터 타입이 같다면 HashMap 객체에 저장해서 전달하면
//        되고 데이터 타입이 다르다면 클래스 객체에 저장해서 전달하면 된다.
        HashMap<String, Integer> hmap = new HashMap<>();
        hmap.put("startNo", guestbookList.getStartNo());
        hmap.put("endNo", guestbookList.getEndNo());
//        mysql은 아래와 같이 endNo 대신 pageSize를 HashMap 객체에 저장한다.
//        hmap.put("pageSize", guestbookList.getPageSize());

//        1페이지 분량의 글 목록을 얻어와서 GuestbookList 클래스 객체의 ArrayList(list)에 저장한다.
        guestbookList.setList(dao.selectList(mapper, hmap));
        //System.out.println(guestbookList);

        return guestbookList;
    }
    
//    selectByIdx.jsp에서 호출되는 수정 또는 삭제할 글번호를 넘겨받고 mapper를 얻어온 후 GuestbookDAO 클래스의
//    글 1건을 얻어오는 메소드를 호출하는 메소드
    public GuestbookVO selectIdx(int idx) throws SQLException {
        System.out.println("SelectService 클래스의 selectIdx() 메소드 실행");
        SqlMapClient mapper = MyAppSqlConfig.getSqlMapInstance();
        GuestbookVO vo = null;
        vo = GuestbookDAO.getInstance().selectByIdx(mapper , idx);
//       얻어온 글 1건을 리턴시킨다.
        return vo;
    }

//    list.jsp에서 호출되는 브라우저에 표시할 페이지 번호와 검색어(내용)을 넘겨받고 mapper를 얻어온 후
//    GuestbookDAO 클래스의 1페이지 분량의 글 목록을 얻어오는 select sql 명령을 실행하는 메소드를 호출하는
//    메소드
    public GuestbookList selectListMemo(int currentPage, String item) throws SQLException {
        System.out.println("SelectService 클래스의 selectListMemo() 메소드 실행");
        SqlMapClient mapper = MyAppSqlConfig.getSqlMapInstance();
        GuestbookList guestbookList = null;
        GuestbookDAO dao = GuestbookDAO.getInstance();
        int pageSize = 10;
        // 내용에 검색어(내용)을 포함하는 글의 개수를 얻어온다.
        int totalCount = dao.selectCountMemo(mapper , item);
        System.out.println(totalCount);
        guestbookList = new GuestbookList(pageSize, totalCount, currentPage);
//        startNo, endNo만 sql 명령으로 넘겨줄 때는 데이터 타입이 같기 때문에 HashMap을 이용했지만
//        category, item을 같이 넘겨야 하므로 데이터 타입이 다르기 때문에 별도의 클래스를 만들고
//        클래스 객체에 데이터를 담아서 넘겨줘야 한다.
        Param param = new Param();
        param.setStartNo(guestbookList.getStartNo());
        param.setEndNo(guestbookList.getEndNo());
        param.setItem(item);

//        내용에 검색어를 포함하는 1페이지 분량의 글을 얻어와서 GuestbookList 클래스의 ArrayList에 저장한다.
        guestbookList.setList(dao.selectListMemo(mapper, param));
        return guestbookList;
    }

    public GuestbookList selectListName(int currentPage, String item) throws SQLException {
        System.out.println("SelectService 클래스의 selectListName() 메소드 실행");
        SqlMapClient mapper = MyAppSqlConfig.getSqlMapInstance();
        GuestbookList guestbookList = null;
        GuestbookDAO dao = GuestbookDAO.getInstance();
        int pageSize = 10;
        int totalCount = dao.selectCountName(mapper , item);
        guestbookList = new GuestbookList(pageSize, totalCount, currentPage);

        Param param = new Param();
        param.setStartNo(guestbookList.getStartNo());
        param.setEndNo(guestbookList.getEndNo());
        param.setItem(item);
        guestbookList.setList(dao.selectListName(mapper, param));
        return guestbookList;
    }

    public GuestbookList selectListMemoName(int currentPage, String item) throws SQLException {
        System.out.println("SelectService 클래스의 selectListMemoName() 메소드 실행");
        SqlMapClient mapper = MyAppSqlConfig.getSqlMapInstance();
        GuestbookList guestbookList = null;
        GuestbookDAO dao = GuestbookDAO.getInstance();

        int pageSize = 10;
        int totalCount = dao.selectCountMemoName(mapper , item);
        System.out.println(totalCount);
        guestbookList = new GuestbookList(pageSize, totalCount, currentPage);

        Param param = new Param();
        param.setStartNo(guestbookList.getStartNo());
        param.setEndNo(guestbookList.getEndNo());
        param.setItem(item);
        guestbookList.setList(dao.selectListMemoName(mapper, param));
        System.out.println(guestbookList);
        return guestbookList;
    }

//    list.jsp에서 호출되는 브라우저에 표시할 페이지 번호와 카테고리(내용, 이름, 내용+이름)을 넘겨받고 mapper를
//    얻어온 후 GuestbookDAO 클래스의 1페이지 분량의 글 목록을 얻어오는 select sql 명령을 실행하는 메소드를 호출하는
//    메소드
    public GuestbookList selectListMulti(int currentPage , String category, String item) throws SQLException {
        System.out.println("SelectService 클래스의 selectListMulti() 메소드 실행");
        SqlMapClient mapper = MyAppSqlConfig.getSqlMapInstance();
        GuestbookList guestbookList = null;
        GuestbookDAO dao = GuestbookDAO.getInstance();

        int pageSize = 10;
//        카테고리에 따른 검색어를 포함하는 글의 개수를 얻어온다.
//        카테고리에 따른 검색어가 포함되었나 조건을 세워야 하기 때문에 Param 클래스 객체를 사용한다.
        Param param = new Param();
        param.setCategory(category);
        param.setItem(item);
        int totalCount = dao.selectCountMulti(mapper, param);

        guestbookList = new GuestbookList(pageSize, totalCount, currentPage);
        param.setStartNo(guestbookList.getStartNo());
        param.setEndNo(guestbookList.getEndNo());
        guestbookList.setList(dao.selectListMulti(mapper, param));
        System.out.println(guestbookList);
        return guestbookList;
    }

}


