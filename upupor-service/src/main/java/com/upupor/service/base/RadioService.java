/*
 * MIT License
 *
 * Copyright (c) 2021-2022 yangrunkang
 *
 * Author: yangrunkang
 * Email: yangrunkang53@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.upupor.service.base;

import com.upupor.data.dao.entity.Radio;
import com.upupor.data.dao.entity.enhance.RadioEnhance;
import com.upupor.data.dto.OperateRadioDto;
import com.upupor.data.dto.page.common.ListRadioDto;
import com.upupor.service.outer.req.AddRadioReq;
import com.upupor.service.outer.req.DelRadioReq;

import java.util.List;

/**
 * 音频服务
 *
 * @author YangRunkang(cruise)
 * @date 2020/11/15 20:31
 */
public interface RadioService {

    Boolean addRadio(Radio radio);

    ListRadioDto listRadioByUserId(Integer pageNum, Integer pageSize, String userId, String searchTitle);

    RadioEnhance getByRadioId(String radioId);

    /**
     * 根据 RadioId 获取集合
     *
     * @param radioIdList
     * @return
     */
    List<Radio> listByRadioIdList(List<String> radioIdList);

    void bindRadioMember(List<RadioEnhance> radioList);

    Integer updateRadio(Radio radio);

    ListRadioDto list(Integer pageNum, Integer pageSize);

    Integer total();

    /**
     * 文章作者是否有电台
     *
     * @param userId
     */
    Boolean userHasRadio(String userId);

    OperateRadioDto createNewRadio(AddRadioReq addRadioReq);

    OperateRadioDto deleteRadio(DelRadioReq delRadioReq);

    /**
     * 绑定电台数据
     *
     * @param radioList
     */
    void bindRadioContentData(List<RadioEnhance> radioList);

}
