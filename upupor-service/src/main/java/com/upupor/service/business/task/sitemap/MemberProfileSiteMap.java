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

package com.upupor.service.business.task.sitemap;

import com.upupor.framework.config.UpuporConfig;
import com.upupor.service.business.task.TaskCommonDataService;
import com.upupor.service.business.task.sitemap.enums.SiteMapType;
import com.upupor.data.dao.entity.Member;
import com.upupor.service.base.MemberService;
import com.upupor.data.dto.seo.GoogleSeoDto;
import com.upupor.data.types.MemberStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yang Runkang (cruise)
 * @date 2022年03月13日 21:30
 * @email: yangrunkang53@gmail.com
 */
@Component
@RequiredArgsConstructor
public class MemberProfileSiteMap extends AbstractSiteMap<Member> {

    private final MemberService memberService;
    private final UpuporConfig upuporConfig;
    private final TaskCommonDataService scheduledCommonService;

    private static final List<String> urlList = new ArrayList<>();

    static {
        urlList.add("/profile/%s/content");
        urlList.add("/profile/%s/radio");
        urlList.add("/profile/%s/message");
        urlList.add("/profile/%s/attention");
        urlList.add("/profile/%s/fans");
    }

    @Override
    protected Boolean dataCheck() {
        return memberService.total() > 0;
    }

    @Override
    public SiteMapType siteMapType() {
        return SiteMapType.MEMBER_PROFILE;
    }

    @Override
    protected List<Member> getSiteMapData() {
        return scheduledCommonService.memberList();
    }

    @Override
    protected void renderSiteMap(List<Member> memberList) {

        String webSite = upuporConfig.getWebsite();

        memberList.forEach(member -> {
            if (member.getStatus().equals(MemberStatus.NORMAL)) {
                for (String url : urlList) {
                    GoogleSeoDto memberProfile = new GoogleSeoDto();
                    String memberProfileUrl = webSite + String.format(url, member.getUserId());
                    memberProfile.setLoc(memberProfileUrl);
                    memberProfile.setChangeFreq("hourly");
                    memberProfile.setLastmod(sdf.format(member.getSysUpdateTime()));
                    memberProfile.setPriority("0.5");// 默认值
                    googleSeoDtoList.add(memberProfile);
                }
            }
        });
    }
}