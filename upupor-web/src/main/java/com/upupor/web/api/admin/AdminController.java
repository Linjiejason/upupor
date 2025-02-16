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

package com.upupor.web.api.admin;

import com.upupor.data.dao.entity.MemberConfig;
import com.upupor.data.dao.entity.enhance.ContentEnhance;
import com.upupor.data.dao.entity.enhance.MemberEnhance;
import com.upupor.data.dao.mapper.CommentMapper;
import com.upupor.data.types.ContentStatus;
import com.upupor.framework.CcResponse;
import com.upupor.service.base.ContentService;
import com.upupor.service.base.MemberService;
import com.upupor.service.listener.event.GenerateGoogleSiteMapEvent;
import com.upupor.service.outer.req.BatchHandleExceptionUserReq;
import com.upupor.service.outer.req.SetContentStatusReq;
import com.upupor.service.outer.req.SetKeywordsReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

import static com.upupor.framework.thread.completable.CompletableFuturePool.ASYNC;


/**
 * 活动控制器
 *
 * @author YangRunkang(cruise)
 * @date 2020/02/05 00:13
 */
@Api(tags = "管理员服务")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final ContentService contentService;
    private final ApplicationEventPublisher eventPublisher;
    private final MemberService memberService;

    @ApiOperation("设置关键字")
    @PostMapping(value = "/content/keywords")
    public CcResponse setKeywords(@RequestBody SetKeywordsReq setKeywordsReq) {
        String contentId = setKeywordsReq.getContentId();
        String keywords = setKeywordsReq.getKeywords();
        ContentEnhance contentEnhance = contentService.getContentByContentIdNoStatus(contentId);
        contentEnhance.getContent().setKeywords(keywords);
        CcResponse ccResponse = new CcResponse();
        ccResponse.setData(contentService.updateContent(contentEnhance));
        return ccResponse;
    }

    @ApiOperation("设置状态")
    @PostMapping(value = "/content/status")
    public CcResponse setContentStatus(@RequestBody SetContentStatusReq setContentStatusReq) {

        String contentId = setContentStatusReq.getContentId();
        ContentStatus status = setContentStatusReq.getStatus();

        ContentEnhance contentEnhance = contentService.getContentByContentIdNoStatus(contentId);
        contentEnhance.getContent().setStatus(status);

        Boolean result = contentService.updateContent(contentEnhance);
        if (result) {
            // 重新生成站点地图
            eventPublisher.publishEvent(new GenerateGoogleSiteMapEvent());
        }

        CcResponse ccResponse = new CcResponse();
        ccResponse.setData(result);
        return ccResponse;
    }

    @ApiOperation("cpu")
    @PostMapping(value = "/cpu")
    public CcResponse cpu() {
        CcResponse ccResponse = new CcResponse();
        ccResponse.setData(ASYNC);
        return ccResponse;
    }

    /**
     * 批量处理异常用户
     *
     * @param batchHandleExceptionUserReq
     * @return
     */
    @ApiOperation("handle-user")
    @PostMapping(value = "/handle-user")
    public CcResponse handleUser(@RequestBody BatchHandleExceptionUserReq batchHandleExceptionUserReq) {
        List<String> userIdList = Arrays.asList(batchHandleExceptionUserReq.getUserId());
        List<MemberEnhance> memberEnhanceList = memberService.listByUserIdList(userIdList);
        if (CollectionUtils.isEmpty(memberEnhanceList)) {
            return new CcResponse(false);
        }

        // 首先设置用户为恶意刷文用户
        memberEnhanceList.forEach(memberEnhance -> {
            memberEnhance.getMember().setStatementId(3); // 主键Id,这里先写死
            memberService.update(memberEnhance);
        });


        // 将用户的所有文章设置为 仅自己可见
        List<ContentEnhance> contentEnhanceList = contentService.listAllByUserId(userIdList);
        if (!CollectionUtils.isEmpty(contentEnhanceList)) {
            contentEnhanceList.forEach(content -> {
                content.getContent().setStatus(ContentStatus.ONLY_SELF_CAN_SEE);
                contentService.updateContent(content);
            });
        }

        // 限制用户发文间隔
        List<MemberConfig> memberConfigList = memberService.listMemberConfig(userIdList);
        if (!CollectionUtils.isEmpty(memberConfigList)) {
            memberConfigList.forEach(memberConfig -> {
                memberConfig.setIntervalTimeCreateContent(3600L * 24);// 对于恶意用户,限制发文调整至24小时
                memberService.updateMemberConfig(memberConfig);
            });
        }

        return new CcResponse(true);
    }

    @Resource
    private CommentMapper commentMapper;


    /**
     * 新增评论楼层数,处理历史数据楼层数据
     *
     * @return
     */
//    @GetMapping("/RefreshCommentBeFloorNumValue")
//    public CcResponse refreshCommentBeFloorNumValue() {
//
//        CcResponse ccResponse = new CcResponse();
//        ccResponse.setData(false);
//
//        LambdaQueryWrapper<Comment> query = new LambdaQueryWrapper<Comment>()
//                .eq(Comment::getStatus, CommentStatus.NORMAL)
//                .orderByAsc(Comment::getCreateTime);
//
//
//        List<Comment> commentList = commentMapper.selectList(query);
//        if (CollectionUtils.isEmpty(commentList)) {
//            return ccResponse;
//        }
//
//        Map<String, List<Comment>> commentIdMap =
//                commentList.stream().collect(Collectors.groupingBy(Comment::getTargetId));
//
//
//        for (String targetId : commentIdMap.keySet()) {
//            List<Comment> targetList = commentIdMap.get(targetId);
//            Long floorNum = 1L;
//            for (Comment comment : targetList) {
//                comment.setFloorNum(floorNum);
//                commentMapper.updateById(comment);
//                floorNum++;
//            }
//        }
//
//
//        ccResponse.setData(true);
//        return ccResponse;
//    }
}
