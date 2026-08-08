package com.example.controlej2e.controller;

import com.example.controlej2e.dto.ComputerRequest;
import com.example.controlej2e.entities.Computer;
import com.example.controlej2e.exception.ComputerNotFoundException;
import com.example.controlej2e.exception.DuplicateMacAddressException;
import com.example.controlej2e.service.ComputerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ComputerController.class)
class ComputerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ComputerService computerService;

    private Computer sampleComputer() {
        return new Computer(1L, "i5", 8, 500, 6000, "00-AA-BB-CC-DD");
    }

    @Test
    void create_returns201WithLocationHeader() throws Exception {
        ComputerRequest request = new ComputerRequest("i5", 8, 500, 6000, "00-AA-BB-CC-DD");
        when(computerService.addComputer(any(Computer.class))).thenReturn(sampleComputer());

        mockMvc.perform(post("/api/computers")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/computers/1"))
                .andExpect(jsonPath("$.macAddress").value("00-AA-BB-CC-DD"));
    }

    @Test
    void create_returns400WhenPayloadIsInvalid() throws Exception {
        ComputerRequest invalid = new ComputerRequest("", -1, -1, -1, "");

        mockMvc.perform(post("/api/computers")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors").exists());
    }

    @Test
    void create_returns409WhenMacAddressAlreadyExists() throws Exception {
        ComputerRequest request = new ComputerRequest("i5", 8, 500, 6000, "00-AA-BB-CC-DD");
        when(computerService.addComputer(any(Computer.class)))
                .thenThrow(new DuplicateMacAddressException("00-AA-BB-CC-DD"));

        mockMvc.perform(post("/api/computers")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void getById_returns200WhenFound() throws Exception {
        when(computerService.getComputerById(1L)).thenReturn(sampleComputer());

        mockMvc.perform(get("/api/computers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.proce").value("i5"));
    }

    @Test
    void getById_returns404WhenNotFound() throws Exception {
        when(computerService.getComputerById(99L)).thenThrow(new ComputerNotFoundException(99L));

        mockMvc.perform(get("/api/computers/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAll_returnsPagedResult() throws Exception {
        Page<Computer> page = new PageImpl<>(List.of(sampleComputer()), PageRequest.of(0, 20), 1);
        when(computerService.getAllComputers(any())).thenReturn(page);

        mockMvc.perform(get("/api/computers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].macAddress").value("00-AA-BB-CC-DD"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void update_returns200WithUpdatedComputer() throws Exception {
        ComputerRequest request = new ComputerRequest("i9", 32, 2000, 15000, "00-AA-BB-CC-DD");
        Computer updated = new Computer(1L, "i9", 32, 2000, 15000, "00-AA-BB-CC-DD");
        when(computerService.updateComputer(eq(1L), any(Computer.class))).thenReturn(updated);

        mockMvc.perform(put("/api/computers/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.proce").value("i9"));
    }

    @Test
    void delete_returns204() throws Exception {
        mockMvc.perform(delete("/api/computers/1"))
                .andExpect(status().isNoContent());

        verify(computerService).deleteComputer(1L);
    }

    @Test
    void delete_returns404WhenNotFound() throws Exception {
        doThrow(new ComputerNotFoundException(99L)).when(computerService).deleteComputer(99L);

        mockMvc.perform(delete("/api/computers/99"))
                .andExpect(status().isNotFound());
    }
}
