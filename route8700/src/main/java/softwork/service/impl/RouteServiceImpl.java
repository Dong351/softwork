package softwork.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softwork.exception.CommonException;
import softwork.mapper.ContestMapper;
import softwork.mapper.RouteMapper;
import softwork.pojo.dto.RouteEditDTO;
import softwork.pojo.entities.Contest;
import softwork.pojo.entities.Route;
import softwork.pojo.entities.User;
import softwork.pojo.vo.RouteAlterVO;
import softwork.pojo.vo.RoutesInfoVO;
import softwork.service.RouteService;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class RouteServiceImpl implements RouteService {
    @Autowired
    RouteMapper routeMapper;
    @Autowired
    ContestMapper contestMapper;

    @Override
    public Object AddRoute(Integer contestId, User user) {
        //限制多次添加
        Route findExist = new Route();
        findExist.setUid(user.getId());
        findExist.setType(1);
        findExist.setData_id(contestId);
        if(routeMapper.selectOne(findExist) != null){
            throw new CommonException("该比赛已在路线中");
        }

        Route route = new Route();
        route.setUid(user.getId());
        route.setType(1);
        route.setData_id(contestId);
        routeMapper.insert(route);
        return null;
    }

    @Override
    public Object GetRoutes(User user) {
        Route findByUid = new Route();
        findByUid.setUid(user.getId());
        List<Route> routes = routeMapper.select(findByUid);
        List<RoutesInfoVO> routesInfoVOS = new ArrayList<>();
        for (Route route:routes){
            RoutesInfoVO routesInfoVO = new RoutesInfoVO();
            BeanUtils.copyProperties(route,routesInfoVO);
            if(routesInfoVO.getRemarks() == null){
                routesInfoVO.setRemarks("快来做属于你的规划吧");
            }

            if(route.getType() == 1){
                Contest contest = contestMapper.GetListVOById(route.getData_id());
                routesInfoVO.setTime(contest.getEnroll_end());
                routesInfoVO.setName(contest.getName());
            }

            routesInfoVOS.add(routesInfoVO);
        }

        ListSort(routesInfoVOS);

        return routesInfoVOS;
    }

    @Override
    public Object EditRemarks(Integer routeId, RouteEditDTO dto, User user) {
        System.out.println(dto);
        Route route = routeMapper.selectByPrimaryKey(routeId);
        System.out.println(route);
        route.setRemarks(dto.getRemarks());
        routeMapper.updateByPrimaryKey(route);
        return null;
    }

    @Override
    public Object RouteAlert(User user) {
        Route findByUid = new Route();
        findByUid.setUid(user.getId());
        List<Route> routes = routeMapper.select(findByUid);
        List<RouteAlterVO> routeAlterVOS = new ArrayList<>();
        for(Route route:routes){
            RouteAlterVO routeAlterVO = new RouteAlterVO();
            BeanUtils.copyProperties(route,routeAlterVO);

            if(route.getType() == 1){
                Contest contest = contestMapper.GetListVOById(route.getData_id());
                routeAlterVO.setName(contest.getName());
                long endTime = contest.getEnroll_end().getTime();
                long nowTime = new Date().getTime();
                if(nowTime > endTime){
                    continue;
                }
                long nd = 1000 * 24 * 60 * 60;
                long diff = endTime - nowTime;
                long days = diff / nd;
                routeAlterVO.setCountdown(String.valueOf(days));
            }

            routeAlterVOS.add(routeAlterVO);
        }
        return routeAlterVOS;
    }

    @Override
    public Object RemoveRoute(Integer type, Integer data_id, User user) {
        Route find = new Route();
        find.setUid(user.getId());
        find.setType(type);
        find.setData_id(data_id);
        Route route = routeMapper.selectOne(find);
        if (route == null){
            throw new CommonException("该比赛/证书不在路线中");
        }
        routeMapper.delete(route);
        return null;
    }

    /**
     * 根据时间排序（其他排序如根据id排序也类似）
     * @param list
     */
    private static void ListSort(List<RoutesInfoVO> list) {
        //用Collections这个工具类传list进来排序
        Collections.sort(list, new Comparator<RoutesInfoVO>() {
            @Override
            public int compare(RoutesInfoVO o1, RoutesInfoVO o2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date dt1 = o1.getTime();
                    Date dt2 = o2.getTime();
                    if (dt1.getTime() > dt2.getTime()) {
                        return 1;//小的放前面
                    }else {
                        return -1;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }

}
