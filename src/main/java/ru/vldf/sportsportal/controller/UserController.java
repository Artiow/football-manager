package ru.vldf.sportsportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.vldf.sportsportal.dto.tourney.TeamDTO;
import ru.vldf.sportsportal.dto.tourney.TeamPlayerDTO;
import ru.vldf.sportsportal.dto.tourney.TourneyDTO;
import ru.vldf.sportsportal.dto.user.UserDTO;
import ru.vldf.sportsportal.service.AdminService;
import ru.vldf.sportsportal.service.AuthService;
import ru.vldf.sportsportal.service.UserService;

import java.util.List;

@Controller
public class UserController {
    private AuthService authService;

    private AdminService adminService;
    private UserService userService;

    @Autowired
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    @Autowired
    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("authUser")
    public UserDTO getAuthUser() {
        return authService.getAuthUser();
    }

    @ModelAttribute("authUserShortName")
    public String getAuthUserShortName() {
        return authService.getAuthUserShortName();
    }

    @GetMapping(value = {"/personalpage"})
    public String toPersonalPage() {
        return "user/personalpage";
    }

//    ==================================================================================
//    === ADMIN

    @GetMapping(value = {"/pp/admin"})
    public String toAdminMenu(ModelMap map) {
        map
                .addAttribute("numOfUnconfirmedUsers", adminService.getUnconfirmedUsersNum())
                .addAttribute("numOfUnconfirmedTeams", adminService.getUnconfirmedTeamsNum())
                .addAttribute("numOfTourneys", adminService.getTourneysNum());

        return "user/admin/menu-admin";
    }

//    ----------------------------------------------------------------------------------
//    --- USER

    @GetMapping(value = {"/pp/admin/check-user"})
    public String toCheckUserPage() {
        int num = adminService.getUnconfirmedUsersNum();
        if (num == 0) return "redirect:/pp/admin";

        UserDTO user = adminService.getFirstUnconfirmedUser();
        String id = user.getId().toString();
        return "redirect:/pp/admin/check-user/user" + id;
    }

    @GetMapping(value = {"/pp/admin/check-user/user{id}"})
    public String toCheckUserPage(@PathVariable("id") int id, ModelMap map) {
        UserDTO user = adminService.getUser(id);
        List<TeamPlayerDTO> duplicates = adminService.getDuplicate(user);

        map
                .addAttribute("duplicates", duplicates)
                .addAttribute("unconfirmedUser", user);

        return "user/admin/page-check-user";
    }

    @GetMapping(value = {"/pp/admin/check-user/user{id}/confirm"})
    public String confirmUser(@PathVariable("id") int id) {
        adminService.confirmUser(id);
        return "redirect:/pp/admin/check-user";
    }

    @GetMapping(value = {"/pp/admin/check-user/user{id}/reject"})
    public String rejectUser(@PathVariable("id") int id) {
        adminService.rejectUser(id);
        return "redirect:/pp/admin/check-user";
    }

    @GetMapping(value = {"/pp/admin/check-user/duplicate{id}/delete"})
    public String deleteDuplicate(@PathVariable("id") int id) {
        adminService.deleteDuplicate(id);
        return "redirect:/pp/admin/check-user";
    }

//    ----------------------------------------------------------------------------------
//    --- TOURNEY

    @GetMapping(value = {"/pp/admin/check-team"})
    public String toConfirmTeamPage(ModelMap map) {
        int num = adminService.getUnconfirmedTeamsNum();
        if (num == 0) return "redirect:/pp/admin";

        map.addAttribute("teams", adminService.getUnconfirmedTeams());
        return "user/admin/page-check-team";
    }

    @GetMapping(value = {"/pp/admin/check-team/team{id}/confirm"})
    public String confirmTeam(@PathVariable("id") int id) {
        adminService.confirmTeam(id);
        return "redirect:/pp/admin/check-team";
    }

    @GetMapping(value = {"/pp/admin/check-team/team{id}/reject"})
    public String rejectTeam(@PathVariable("id") int id) {
        adminService.rejectTeam(id);
        return "redirect:/pp/admin/check-team";
    }

    @GetMapping(value = {"/pp/admin/tourney"})
    public String toTourneyCatalogPage(ModelMap map) {
        map.addAttribute("tourneysList", adminService.getTourneysList());
        return "user/admin/page-tourney-catalog";
    }

    @GetMapping(value = {"/pp/admin/tourney/tourney{id}"})
    public String toTourneyPage(@PathVariable("id") int id, ModelMap map) {
        TourneyDTO tourneyDTO = adminService.getTourney(id);
        map.addAttribute("tourneyDTO", tourneyDTO);

        int status = tourneyDTO.getStatus().getId();
        switch (status) {
            case 1: return "user/admin/page-tourney-status-formed"; //TOURNEY_FORMED
            case 2: return "user/admin/page-tourney-status-ready"; //TOURNEY_READY
            default: return "redirect:/pp/admin/tourney";
        }
    }

    @GetMapping(value = {"/pp/admin/tourney/create-tourney"})
    public String toCreateTourneyForm(ModelMap map) {
        map.addAttribute("tourneyDTO", new TourneyDTO()); //TODO: delete this line?
        return "user/admin/form-create-tourney";
    }

    @PostMapping(value = {"/pp/admin/tourney/create-tourney"})
    public String createTourney(@ModelAttribute(value="tourneyDTO") TourneyDTO tourneyDTO) {
        adminService.createTourney(tourneyDTO);
        return "redirect:/pp/admin/tourney";
    }

//    ==================================================================================
//    === TOURNEY

    @GetMapping(value = {"/pp/tourney"})
    public String toTourneyMenu(ModelMap map) {
        map.addAttribute("teamList", userService.getTeamList());
        return "user/tourney/menu-tourney";
    }

    @GetMapping(value = {"/pp/tourney/team{id}"})
    public String toTeamPage(@PathVariable("id") int id, ModelMap map) {
        map.addAttribute("teamDTO", userService.getTeam(id));
        return "user/tourney/page-team";
    }

    @GetMapping(value = {"/pp/tourney/create-team"})
    public String toCreateTeamForm(ModelMap map) {
        map.addAttribute("teamDTO", new TeamDTO()); //TODO: delete this line?
        return "user/tourney/form-create-team";
    }

    @PostMapping(value = {"/pp/tourney/create-team"})
    public String createTeam(@ModelAttribute(value="teamDTO") TeamDTO teamDTO) {
        userService.createTeam(teamDTO);
        return "redirect:/pp/tourney";
    }
}