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

package com.upupor.data.dto.page.comment;

import com.upupor.data.dao.entity.enhance.CommentEnhance;
import lombok.Builder;
import lombok.Data;

/**
 * @author Yang Runkang (cruise)
 * @date 2023年01月09日 09:50
 * @email: yangrunkang53@gmail.com
 */
@Data
@Builder
public class CommentDto {
    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 目标Url
     */
    private String targetUrl;

    /**
     * 目标标题
     */
    private String targetTitle;

    private CommentEnhance commentEnhance;

    /**
     * 来源标签
     */
    private String sourceTag;

    public static CommentDto create(String commentContent,
                                    String targetUrl,
                                    String targetTitle,
                                    CommentEnhance commentEnhance,
                                    String sourceTag) {
        return CommentDto.builder()
                .commentContent(commentContent)
                .targetUrl(targetUrl)
                .targetTitle(targetTitle)
                .commentEnhance(commentEnhance)
                .sourceTag(sourceTag)
                .build();
    }
}
