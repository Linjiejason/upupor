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

package com.upupor.web.page.view_data.content;

import com.upupor.data.dao.entity.Content;
import com.upupor.data.dto.page.ContentIndexDto;
import com.upupor.data.utils.PageUtils;
import com.upupor.framework.CcConstant;
import com.upupor.framework.utils.CcUtils;
import com.upupor.service.base.CommentService;
import com.upupor.service.business.content.PublishedContent;
import com.upupor.web.page.view_data.AbstractView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

import static com.upupor.framework.CcConstant.ContentView.CONTENT_INDEX;
import static com.upupor.framework.CcConstant.Page.SIZE;

/**
 * @author Yang Runkang (cruise)
 * @date 2022年02月09日 22:55
 * @email: yangrunkang53@gmail.com
 */
@Component
@RequiredArgsConstructor
public class ContentDetailView extends AbstractView {
    public static final String URL = "/u/{contentId}";
    private final PublishedContent publishedContent;
    private final CommentService commentService;


    @Override
    public String viewName() {
        return CONTENT_INDEX;
    }

    @Override
    protected String pageUrl() {
        return URL;
    }

    @Override
    public String adapterUrlToViewName(String pageUrl) {
        if (pageUrl.startsWith("/u/")) {
            return viewName();
        }
        return pageUrl;
    }

    @Override
    protected void seoInfo() {
        for (Object value : modelAndView.getModelMap().values()) {
            if (value instanceof ContentIndexDto) {
                ContentIndexDto contentIndexDto = (ContentIndexDto) value;
                Content content = contentIndexDto.getContentEnhance().getContent();

                modelAndView.addObject(CcConstant.SeoKey.TITLE, content.getTitle());
                modelAndView.addObject(CcConstant.SeoKey.DESCRIPTION, content.getTitle());
                // 文章详情设置关键字
                if (!StringUtils.isEmpty(content.getKeywords())) {
                    modelAndView.addObject(CcConstant.SeoKey.KEYWORDS, content.getKeywords());
                } else {
                    modelAndView.addObject(CcConstant.SeoKey.KEYWORDS, CcUtils.getSegmentResult(content.getTitle()));
                }
                break;
            }
        }

    }

    @Override
    protected void fetchData() {
        Integer pageNum = query.getPageNum();
        Integer pageSize = query.getPageSize();
        String contentId = query.getContentId();

        ContentIndexDto contentIndexDto = publishedContent.pageContentIndexDto(contentId, pageNum, pageSize);
        modelAndView.addObject(contentIndexDto);
    }

    @Override
    protected void specifyPage() {
        Integer pageNum = query.getPageNum();
        Integer pageSize = query.getPageSize();
        String contentId = query.getContentId();

        // 默认显示30条评论
        if (Objects.isNull(pageNum)) {
            // 获取最新的评论
            Integer count = commentService.countByTargetId(contentId);
            pageNum = PageUtils.calcMaxPage(count, SIZE);
            query.setPageNum(pageNum);
        }
        if (Objects.isNull(pageSize)) {
            pageSize = CcConstant.Page.SIZE;
            query.setPageSize(pageSize);
        }
    }

    @Override
    protected Boolean isNeedSpecifyPage() {
        return Boolean.TRUE;
    }
}
