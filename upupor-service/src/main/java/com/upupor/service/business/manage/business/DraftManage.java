/*
 * <!--
 *   ~ MIT License
 *   ~
 *   ~ Copyright (c) 2021-2022 yangrunkang
 *   ~
 *   ~ Author: yangrunkang
 *   ~ Email: yangrunkang53@gmail.com
 *   ~
 *   ~ Permission is hereby granted, free of charge, to any person obtaining a copy
 *   ~ of this software and associated documentation files (the "Software"), to deal
 *   ~ in the Software without restriction, including without limitation the rights
 *   ~ to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   ~ copies of the Software, and to permit persons to whom the Software is
 *   ~ furnished to do so, subject to the following conditions:
 *   ~
 *   ~ The above copyright notice and this permission notice shall be included in all
 *   ~ copies or substantial portions of the Software.
 *   ~
 *   ~ THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   ~ IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   ~ FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   ~ AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   ~ LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   ~ OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *   ~ SOFTWARE.
 *   -->
 */

package com.upupor.service.business.manage.business;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.upupor.data.dao.entity.Draft;
import com.upupor.data.dao.entity.enhance.ContentEnhance;
import com.upupor.data.dto.dao.ListDraftDto;
import com.upupor.data.dto.page.common.ListContentDto;
import com.upupor.framework.CcConstant;
import com.upupor.service.base.ContentService;
import com.upupor.service.base.DraftService;
import com.upupor.service.business.manage.AbstractManage;
import com.upupor.service.business.manage.ManageDto;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cruise
 * @createTime 2021-12-24 18:03
 */
@Component
public class DraftManage extends AbstractManage {

    @Resource
    private DraftService draftService;

    @Resource
    private ContentService contentService;

    @Override
    protected void specifyDtoHandle(ManageDto manageDto) {
        Integer pageNum = manageDto.getPageNum();
        Integer pageSize = manageDto.getPageSize();
        String userId = manageDto.getUserId();
        String searchTitle = manageDto.getSearchTitle();
        String searchContentId = manageDto.getSearchContentId();

        ListDraftDto queryDto = ListDraftDto.builder()
                .userId(userId)
                .searchTitle(searchTitle)
                .draftId(searchContentId)
                .build();
        PageHelper.startPage(pageNum, pageSize);
        List<Draft> draftList = draftService.listByDto(queryDto);
        PageInfo<Draft> pageInfo = new PageInfo<>(draftList);

        ListContentDto listContentDto = new ListContentDto(pageInfo);
        listContentDto.setContentEnhanceList(parseToContentList(draftList));
        contentService.bindContentTag(listContentDto.getContentEnhanceList());
        getMemberIndexDto().setListContentDto(listContentDto);

    }


    private List<ContentEnhance> parseToContentList(List<Draft> draftList) {
        List<ContentEnhance> contentList = new ArrayList<>();
        if (CollectionUtils.isEmpty(draftList)) {
            return new ArrayList<>();
        }
        draftList.forEach(draft -> {
            ContentEnhance contentEnhance = Draft.parseContent(draft);
            contentEnhance.setExistedContent(contentService.exists(contentEnhance.getContent().getContentId()));
            contentList.add(contentEnhance);
        });
        return contentList;
    }


    @Override
    public String viewName() {
        return CcConstant.UserManageView.USER_MANAGE_DRAFT;
    }

    @Override
    public String viewDesc() {
        return "草稿箱";
    }
}
