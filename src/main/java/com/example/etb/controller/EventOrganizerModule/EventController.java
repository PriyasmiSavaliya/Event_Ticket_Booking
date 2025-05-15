package com.example.etb.controller.EventOrganizerModule;

import com.example.etb.Constant.Config.PathConstant;
import com.example.etb.model.EventOrganizerModule.EventModel;
import com.example.etb.service.EventOrganizerModule.EventService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping(PathConstant.EVENT_PATH)
public class EventController {

    @Autowired
    private EventService eventService;

    // Show Add Event Page
    // Show Add Event Page
    @GetMapping("/add")
    public String showAddEvent(Model model, HttpSession session) {
        model.addAttribute("event", new EventModel());
        model.addAttribute("eventOrganizerId", session.getAttribute("userId"));
        return "EventOrganizerModule/Event/createevent";
    }

    // Handle Add Event
    @PostMapping("/add")
    public String addEvent(@Validated @ModelAttribute EventModel event,
                           BindingResult bindingResult,
                           @RequestParam(value = "image", required = false) MultipartFile image,
                           RedirectAttributes redirectAttributes,
                           HttpSession httpSession) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Validation failed.");
            return "redirect:" + PathConstant.EVENT_PATH + "/add";
        }

        // Image Upload Handling
        if (image != null && !image.isEmpty()) {
            try {
                String uploadDir = "src/main/resources/static/uploads";
                File uploadPath = new File(uploadDir);
                if (!uploadPath.exists()) {
                    uploadPath.mkdirs();
                }

                String fileExtension = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf("."));
                String uniqueFileName = UUID.randomUUID() + fileExtension;
                Path path = Paths.get(uploadDir, uniqueFileName);
                Files.write(path, image.getBytes());
                event.setEventImage(uniqueFileName);

            } catch (IOException e) {
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("error", "Failed to upload image.");
                return "redirect:" + PathConstant.EVENT_PATH + "/add";
            }
        }

        Long eventOrganizerId = (Long) httpSession.getAttribute("userId");
        eventService.saveEvent(event, eventOrganizerId);
        redirectAttributes.addFlashAttribute("message", "Event saved successfully!");
        return "redirect:" + PathConstant.EVENT_PATH + "/list";
    }



    // Show Edit Event Page
    @GetMapping("/edit/{id}")
    public String editEvent(@PathVariable Long id, Model model) {
        EventModel event = eventService.getEventById(id);
        if (event == null) {
            return "redirect:" + PathConstant.EVENT_PATH + "/list";
        }

        // Format dates and times for Thymeleaf
        String formattedDate = (event.getEventDate() != null) ? event.getEventDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "";
        String formattedStartTime = (event.getStartTime() != null) ? event.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")) : "";
        String formattedEndTime = (event.getEndTime() != null) ? event.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")) : "";

        model.addAttribute("events", event);
        model.addAttribute("formattedDateForInput", formattedDate);
        model.addAttribute("formattedStartTime", formattedStartTime);
        model.addAttribute("formattedEndTime", formattedEndTime);
        if (event.getEventOrganizer() != null) {
            model.addAttribute("eventOrganizerId", event.getEventOrganizer().getId());
        } else {
            // Handle the case where eventOrganizer is null (optional)
            model.addAttribute("eventOrganizerId", null);
        }
        return "EventOrganizerModule/Event/editevent";
    }

    // Handle Update Event
    @PostMapping("/edit")
    public String updateEvent(@RequestParam("id") Long id, @ModelAttribute("events") EventModel event, @RequestParam("eventDate") String eventDate, @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime, @RequestParam(value = "eventOrganizerId") Long eventOrganizerId, @RequestParam(value = "image", required = false) MultipartFile image, RedirectAttributes redirectAttributes) {

        EventModel existingEvent = eventService.getEventById(id);
        if (existingEvent == null) {

            redirectAttributes.addFlashAttribute("error", "Event not found.");
            return "redirect:" + PathConstant.EVENT_PATH + "/list";
        }

        // Update fields
        existingEvent.setEventName(event.getEventName());
        existingEvent.setEventDescription(event.getEventDescription());
        existingEvent.setEventAddress(event.getEventAddress());
        existingEvent.setTotalTicket(event.getTotalTicket());

        // Convert and set date/time fields
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        existingEvent.setEventDate(LocalDate.parse(eventDate, dateFormatter));
        existingEvent.setStartTime(LocalTime.parse(startTime, timeFormatter));
        existingEvent.setEndTime(LocalTime.parse(endTime, timeFormatter));

        // Handle Image Upload (only if new image is uploaded)
        if (image != null && !image.isEmpty()) {
            try {
                String uploadDir = "src/main/resources/static/uploads";
                File uploadPath = new File(uploadDir);
                if (!uploadPath.exists()) {
                    uploadPath.mkdirs();
                }

                String fileExtension = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf("."));
                String uniqueFileName = UUID.randomUUID() + fileExtension;
                Path path = Paths.get(uploadDir, uniqueFileName);
                Files.write(path, image.getBytes());
                existingEvent.setEventImage(uniqueFileName);

            } catch (IOException e) {
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("error", "Failed to upload image.");
                return "redirect:" + PathConstant.EVENT_PATH + "/edit/" + id;
            }
        }

        eventService.saveEvent(existingEvent, eventOrganizerId);
        redirectAttributes.addFlashAttribute("message", "Event updated successfully!");
        return "redirect:" + PathConstant.EVENT_PATH + "/list";
    }

    // Delete Event
    @GetMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        eventService.deleteEvent(id);
        redirectAttributes.addFlashAttribute("message", "Event deleted successfully!");
        return "redirect:" + PathConstant.EVENT_PATH + "/list";
    }

    // Show Event List - Only show events for the logged-in organizer
    @GetMapping("/list")
    public String showEvents(Model model, HttpSession session) {
        Long eventOrganizerId = (Long) session.getAttribute("userId");
        List<EventModel> events = eventService.getEventsByOrganizerId(eventOrganizerId);
        model.addAttribute("events", events);
        return "EventOrganizerModule/Event/listevent";
    }

}
