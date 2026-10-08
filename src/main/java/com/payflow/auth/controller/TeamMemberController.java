package com.payflow.auth.controller;

import com.payflow.auth.dto.CreateTeamMemberRequest;
import com.payflow.auth.dto.TeamMemberResponse;
import com.payflow.auth.dto.UpdateTeamMemberRequest;
import com.payflow.auth.service.TeamMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/team-members")
@RequiredArgsConstructor
public class TeamMemberController {

    private final TeamMemberService teamMemberService;

    @PostMapping
    public ResponseEntity<TeamMemberResponse> createTeamMember(
            @Valid @RequestBody CreateTeamMemberRequest request,
            @RequestParam Long companyId
    ) {

        TeamMemberResponse response =
                teamMemberService.createTeamMember(request, companyId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<TeamMemberResponse>> getTeamMembers(
            @RequestParam Long companyId
    ) {

        List<TeamMemberResponse> teamMembers =
                teamMemberService.getTeamMembers(companyId);

        return ResponseEntity.ok(teamMembers);
    }

    @PatchMapping("/{userId}/status")
    public ResponseEntity<TeamMemberResponse> updateStatus(
            @PathVariable Long userId,
            @RequestParam boolean enabled
    ) {

        TeamMemberResponse response =
                teamMemberService.updateStatus(userId, enabled);

        return ResponseEntity.ok(response);
    }


    @PutMapping("/{userId}")
    public ResponseEntity<TeamMemberResponse> updateTeamMember(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateTeamMemberRequest request
    ) {

        TeamMemberResponse response =
                teamMemberService.updateTeamMember(userId, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteTeamMember(
            @PathVariable Long userId
    ) {

        teamMemberService.deleteTeamMember(userId);

        return ResponseEntity.noContent().build();
    }

}