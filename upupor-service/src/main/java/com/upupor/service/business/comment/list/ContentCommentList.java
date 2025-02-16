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

package com.upupor.service.business.comment.list;

import com.upupor.data.dao.entity.Comment;
import com.upupor.data.dao.entity.Content;
import com.upupor.data.dao.entity.enhance.CommentEnhance;
import com.upupor.data.dao.entity.enhance.ContentEnhance;
import com.upupor.data.dto.page.comment.CommentDto;
import com.upupor.data.types.ContentType;
import com.upupor.service.base.ContentService;
import com.upupor.service.business.comment.list.abstracts.AbstractCommentList;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Yang Runkang (cruise)
 * @date 2023年01月09日 15:01
 * @email: yangrunkang53@gmail.com
 */
@Component
public class ContentCommentList extends AbstractCommentList<Content> {

    @Resource
    private ContentService contentService;

    @Override
    protected Boolean isFit(ContentType commentSource) {
        return ContentType.isContent(commentSource);
    }

    @Override
    protected List<String> filterTargetIdList() {
        return commentEnhanceList.stream()
                .map(CommentEnhance::getComment)
                .filter(s -> ContentType.isContent(s.getCommentSource()))
                .map(Comment::getTargetId)
                .collect(Collectors.toList());
    }

    @Override
    protected Map<String, Content> filteredIdConvertToTMap(List<String> filteredIdList) {
        Map<String, Content> contentMap = new HashMap<>();
        List<ContentEnhance> contentEnhances = contentService.listByContentIdList(filteredIdList);
        contentEnhances.stream()
                .map(ContentEnhance::getContent)
                .forEach(s -> contentMap.putIfAbsent(s.getContentId(), s))
        ;
        return contentMap;
    }

    @Override
    protected void createCommentDto(Map<String, Content> contentMap, CommentEnhance commentEnhance) {
        Comment comment = commentEnhance.getComment();
        String targetId = comment.getTargetId();

        Content content = contentMap.get(targetId);
        if (Objects.isNull(content)) {
            return;
        }
        commentDtoList.add(CommentDto.create(comment.getCommentContent(),
                "/u/" + content.getContentId() + calcPageAnchor(comment.getFloorNum()),
                content.getTitle(),
                commentEnhance,
                "文章"
        ));
    }
}
