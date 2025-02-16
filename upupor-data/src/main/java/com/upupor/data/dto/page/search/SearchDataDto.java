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

package com.upupor.data.dto.page.search;

import com.upupor.data.dao.entity.enhance.MemberEnhance;
import com.upupor.framework.utils.CcDateUtil;
import com.upupor.lucene.enums.LuceneDataType;
import lombok.Data;

import java.util.Objects;

/**
 * 搜索数据Dto
 *
 * @author Yang Runkang (cruise)
 * @date 2022年04月04日 14:46
 * @email: yangrunkang53@gmail.com
 */
@Data
public class SearchDataDto {

    /**
     * 搜索结果类型
     */
    private LuceneDataType dataType;

    /**
     * 搜索结果标题
     */
    private String resultTitle;

    /**
     * 搜索结果Id
     */
    private String resultId;

    /**
     * 结果所属用户对象绑定
     */
    private MemberEnhance memberEnhance;

    /**
     * 资源地址
     */
    private String hrefUrl;

    /**
     * 创建时间
     */
    private Long createTime;
    private String createDate;

    public String getCreateDate() {
        if (Objects.isNull(createTime)) {
            return createDate;
        }
        return CcDateUtil.timeStamp2Date(createTime);
    }
}
