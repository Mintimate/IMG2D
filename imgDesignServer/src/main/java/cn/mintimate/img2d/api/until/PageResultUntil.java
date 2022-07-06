package cn.mintimate.img2d.api.until;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页结果工具
 *
 * @author Mintimate
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResultUntil<T>{
    /**
     * 总数
     */
    private long count;
    /**
     * 分页列表
     */
    private List<T> recordList;
}
