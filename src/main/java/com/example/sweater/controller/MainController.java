package com.example.sweater.controller;

import com.example.sweater.domain.Message;
import com.example.sweater.domain.User;
import com.example.sweater.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

// Контроллер это программный модуль который слушает запросы по пути("/greeting") и возвращает что - то
// В данном случае greeting - шаблон
// Model - это то куда мы будем складывать данные, которые мы хотим вернуть пользователю
@Controller
public class MainController {
    // C этой ссылочной переменной мы будем доставать данные из БД и сохранять их при помощи методов
    @Autowired
    private MessageRepo messageRepo;
    // Таким образом указываем Spring, что хотим получить какую-то переменную
    // Он выдергивает из контекста какие-то значения
    // В текущей ситуации он ищёт properties upload.path и вставляет в эту переменную
    @Value("${upload.path}")
    private String uploadPath;

    // В строке запроса в браузере, после того как указали name = ?, значение ? записывается в наш параметр name
    // В value map кладём то, что получили из параметра и передаём в шаблон
    @GetMapping("/")
    public String greeting() {
        // Далее отображаем
        return "greeting";
    }

    // Будет возвращать список по дефолту из main
    @GetMapping("/main")
    public String main(@RequestParam(name = "filter", required = false, defaultValue = "") String filter, Model model) {
        Iterable<Message> messages;

        if (filter != null && !filter.isEmpty()) {
            messages = messageRepo.findByTag(filter);
        } else {
            messages = messageRepo.findAll();
        }

        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);
        return "main";
    }

    // Будет брать данные из  <form method="post"> записывать и возвращать список из БД
    // форма будет отправляться туда же откуда пришла
    @PostMapping("/main")
    // Анотация @RequestParam выдергивает из запросов или из формы, если передаём post ну либо из url(как в GetMapping)
    // - значения
    // То есть, Spring пытается выдернуть поля по имени, которое указано в параметре
    public String add(
            @AuthenticationPrincipal User user,
            @Valid Message message,
            // Это список аргументов и сообщений ошибок валидации
            // Данный аргумент, должен всегда идти перед Model
            BindingResult bindingResult,
            Model model,
            @RequestParam("file") MultipartFile file) throws IOException {
        message.setAuthor(user);
        if (bindingResult.hasErrors()) {
            // Если наше сообщение message, не соотвествует требованиям, которые мы указали в Message
            // то мы получим сообщение в errors Map
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            model.addAttribute("message", message);
        } else {
            if (file != null) {
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }
                String uuidFile = UUID.randomUUID().toString();
                String resultFileName = uuidFile + "." + file.getOriginalFilename();
                file.transferTo(new File(uploadPath + "/" + resultFileName));
                message.setFilename(resultFileName);
            }
            model.addAttribute("message", null);
            // Обновляем нашу запись в БД, передавая наш объект с данными, которые получили из формы
            messageRepo.save(message);
        }

        // Берём данные из репозитория
        Iterable<Message> messages = messageRepo.findAll();
        // положили в map
        model.addAttribute("messages", messages);
        // отдали пользователю
        return "main";
    }

    // {message} - таким образом получаем id
    @GetMapping("/delete/{message}")
    public String delete(@PathVariable Message message, Model model) {
        model.addAttribute("message", message);
        return "delete";
    }

    @PostMapping("/delete")
    public String deleteMessage(
            @RequestParam("messageId") Message message) {
        //Message message = messageRepo.findById(deleteId);
        messageRepo.delete(message);
        return "redirect:/main";
    }
}