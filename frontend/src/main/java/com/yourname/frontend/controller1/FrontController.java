package com.yourname.frontend.controller1;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.yourname.backen.entity.TrainNumber;
import com.yourname.backen.entity.TrainNumberDetail;
import com.yourname.backen.exception.BusinessException;
import com.yourname.backen.mapper.TrainNumberDetailMapper;
import com.yourname.backen.mapper.TrainNumberMapper;
import com.yourname.frontend.Dto1.TrainNumberLeftDto;
import com.yourname.frontend.param.SearchCountLeftParam;
import com.yourname.frontend.util.BeanValidator;
import com.yourname.sync.common.TrainESConstant;
import com.yourname.sync.service.EsService;
import org.apache.commons.collections.MapUtils;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description TODO
 * @Author 你的名字
 * @Date 2022/12/6 13:52
 */
@Controller
public class FrontController {

    @Resource
    TrainNumberDetailMapper trainNumberDetailMapper;

    @Resource
    TrainNumberMapper trainNumberMapper;

    /**
     * @Description 查询余票
     * @Author 你的名字
     * @Date 2022/12/6 13:54
     */
    @ResponseBody
    @RequestMapping("searchCountLeft")
    public R searchCountLeft(SearchCountLeftParam param) {
        return R.success();
    }


    private List<TrainNumberLeftDto> searchCountLeftService(SearchCountLeftParam param) throws Exception {

        //对参数进行校验
        BeanValidator.check(param);
        ArrayList<TrainNumberLeftDto> dtoList = Lists.newArrayList();
        GetRequest getRequest = new GetRequest(TrainESConstant.INDEX, TrainESConstant.TYPE, param.getFromStationId()
                + "_" + param.getToStationId());
        EsService esService = new EsService();
        GetResponse getResponse = esService.get(getRequest);
        if (getResponse == null) {
            throw new BusinessException("es数据查询失败，请重试");
        }
        Map<String, Object> map = getResponse.getSourceAsMap();
        if (MapUtils.isEmpty(map)) {
            return dtoList;
        } else {
            String trainNumbers = (String) map.get(TrainESConstant.COLUMN_TRAIN_NUMBER);
            List<String> numberList = Splitter.on(".").omitEmptyStrings().splitToList(trainNumbers);
            numberList.parallelStream().forEach(trainNumberName -> {
                TrainNumber trainNumber = trainNumberMapper.selectByName(trainNumberName);
                List<TrainNumberDetail> trainNumberDetailList = trainNumberDetailMapper.
                        selectAllByTrainNumberIdOrderByStationIndex(trainNumber.getId());
                Map<Integer, TrainNumberDetail> map1 = trainNumberDetailList.stream().
                        collect(Collectors.toMap(TrainNumberDetail::getFromStationId, t -> t));
            });
        }
        return dtoList;
    }
}
