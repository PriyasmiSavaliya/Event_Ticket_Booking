package com.example.etb.controller.AdminModule;

import com.example.etb.Constant.Config.PathConstant;
import com.example.etb.model.EventOrganizerModule.EventModel;
import com.example.etb.model.UserModel;
import com.example.etb.service.EventOrganizerModule.EventService;
import com.example.etb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(PathConstant.ADMIN_PATH)
public class AdminDashboardController {

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    @GetMapping("/dashboard")
    public String adminModule() {
        return "AdminModule/admin-dashboard";
    }

    @GetMapping("/manage-customers")
    public String manageUsers(Model model) {
        List<UserModel> customers = userService.getAllOrganizations("CUSTOMER");  // Assuming method exists in UserService
        model.addAttribute("customers", customers);
        return "AdminModule/manage-users"; // HTML page for managing users
    }

    @GetMapping("/manage-events")
    public String manageEvents(Model model) {
        List<EventModel> events = eventService.getAllEvents(); // Assuming method exists in EventService
        model.addAttribute("events", events);
        return "AdminModule/manage-events"; // HTML page for managing events
    }

    @GetMapping("/manage-organizations")
    public String manageOrganizations(Model model) {
        List<UserModel> organizations = userService.getAllOrganizations("EVENT_ORGANIZER"); // Assuming method exists in UserService
        model.addAttribute("organizations", organizations);
        return "AdminModule/manage-event-organisers"; // HTML page for managing organizations
    }
}
