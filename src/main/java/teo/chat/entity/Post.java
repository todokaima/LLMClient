package teo.chat.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String field1;

    @Column(length = 20, nullable = false)
    private String field2;

    @Column(length = 20, nullable = false)
    private String field3;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Post() {}

    public Post(String field1, String field2, String field3) {
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
    }

    public Long getId() { return id; }
    public String getField1() { return field1; }
    public void setField1(String field1) { this.field1 = field1; }
    public String getField2() { return field2; }
    public void setField2(String field2) { this.field2 = field2; }
    public String getField3() { return field3; }
    public void setField3(String field3) { this.field3 = field3; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
