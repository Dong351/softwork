package softwork.pojo.vo;

import lombok.Data;

@Data
public class CollectCertificateListVO {
    private Integer certificateId;
    private Integer status;
    private String restTime;
    private String certificateName;
    private Integer views;
    private Integer collections;
}
