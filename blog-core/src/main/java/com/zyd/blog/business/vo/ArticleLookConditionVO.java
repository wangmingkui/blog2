package com.zyd.blog.business.vo;

import com.zyd.blog.business.entity.ArticleLook;
import com.zyd.blog.framework.object.BaseConditionVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/16 16:26
 * @since 1.0
 */

/**
 * ArticleLookConditionVO继承父类BaseConditionVO属性，在比较时不涉及父类成员，只涉及子类属性
 * @param 文章浏览
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ArticleLookConditionVO extends BaseConditionVO {
	private ArticleLook articleLook;
}

