package com.example.demo.models;



import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.Instant;

@Entity
@Table(name = "user_images")
public class UserImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // original file name
    @Column(nullable = false)
    private String filename;

    // MIME type, e.g. image/jpeg
    @Column(nullable = false)
    private String contentType;

    // file size in bytes
    @Column(nullable = false)
    private Long size;

    // actual bytes stored as bytea in PostgreSQL
    @Lob
    @Column(nullable = false, columnDefinition = "bytea")
    private byte[] data;

    @CreationTimestamp
    private Instant createdAt;

    // optional: if you want bidirectional link to User
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    // getters / setters (or use Lombok)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }

    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }

    public Long getSize() { return size; }
    public void setSize(Long size) { this.size = size; }

    public byte[] getData() { return data; }
    public void setData(byte[] data) { this.data = data; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
