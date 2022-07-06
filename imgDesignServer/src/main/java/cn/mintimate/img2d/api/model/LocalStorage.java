package cn.mintimate.img2d.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * QQ返回给前台的localStorage对象
 *
 * @author Mintimate
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocalStorage {
    /**
     * 存储信息
     */
    private String data;
    /**
     * 有效期，1000=1s
     */
    private long expired;
    /**
     * 有效起始时间
     */
    private long time;
}
