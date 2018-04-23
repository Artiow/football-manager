package ru.vldf.sportsportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import ru.vldf.sportsportal.dto.common.UserDTO;
import ru.vldf.sportsportal.dto.tourney.CompositionStatisticDTO;
import ru.vldf.sportsportal.dto.tourney.GameDTO;
import ru.vldf.sportsportal.dto.tourney.TourneyDTO;
import ru.vldf.sportsportal.service.AuthService;
import ru.vldf.sportsportal.service.TourneyService;

import java.util.List;

@Controller
public class TourneyController {
    private AuthService authService;
    private TourneyService tourneyService;

    @Autowired
    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    @Autowired
    public void setTourneyService(TourneyService tourneyService) {
        this.tourneyService = tourneyService;
    }

    @ModelAttribute("authUser")
    public UserDTO getAuthUser() {
        return authService.getAuthUser();
    }

    @GetMapping(value = {"/tourney"})
    public String toCoverPage(ModelMap map) {
//        return "tourney/cover";
        return "redirect:/tourney3/score"; //TODO: wtf
    }

    @GetMapping(value = {"/tourney{id}/score"})
    public String toTourneyPage(@PathVariable("id") int id, ModelMap map) {
        TourneyDTO tourney = tourneyService.getTourney(id);
        map.addAttribute("tourneyCur", tourney);
        map.addAttribute("tourneyList", tourneyService.getTourneyList());

        List<GameDTO> gameList = tourneyService.getGameList(tourney);
        map.addAttribute("gameList", gameList);

        List<Integer[]> matches = tourneyService.getResults(gameList);
        map.addAttribute("matchList", matches);

        return "tourney/cover-tourney-score";
    }

    @GetMapping(value = {"/tourney{id}/table"})
    public String toTourneyTablePage(@PathVariable("id") int id, ModelMap map) {
        TourneyDTO tourney = tourneyService.getTourney(id);
        map.addAttribute("tourneyCur", tourney);
        map.addAttribute("tourneyList", tourneyService.getTourneyList());

        List<CompositionStatisticDTO> statistics = tourneyService.getStatistics(tourney);
        map.addAttribute("statList", statistics);

        return "tourney/cover-tourney-table";
    }
}