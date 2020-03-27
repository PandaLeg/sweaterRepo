package com.example.sweater.domain;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

// Этот класс определяет типы данных, которые будут сохранены в базе данных при вызове метода save ()
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // @NotBlank - применяется только к строкам и проверяет не пуста ли она
    // Если будет нарушено правило @NotBlank, то он выведет сообщение PLease...
    // Если длинна поля текст превысит 2048, то Message...
    @NotBlank(message = "Please fill the message")
    @Length(max = 2048, message = "Message to long(more than 2kB)")
    private String text;
    @NotBlank(message = "Please fill the tag")
    @Length(max = 255, message = "Message to long(more than 255)")
    private String tag;

    // Fetch - Каждый раз когда мы получаем сообщение, мы хотим получать информацию об авторе
    @ManyToOne(fetch = FetchType.EAGER)
    // Нужно чтобы в БД поле называлось user_id, а не author_id
    @JoinColumn(name = "user_id")
    private User author;

    private String filename;

    public Message() {
    }

    public Message(String text, String tag, User user) {
        this.author = user;
        this.text = text;
        this.tag = tag;
    }

    public String getAuthorName() {
        return author != null ? author.getUsername() : "<none>";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
