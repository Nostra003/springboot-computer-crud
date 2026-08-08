package com.example.controlej2e.service;

import com.example.controlej2e.dao.ComputerRepository;
import com.example.controlej2e.entities.Computer;
import com.example.controlej2e.exception.ComputerNotFoundException;
import com.example.controlej2e.exception.DuplicateMacAddressException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComputerManagerTest {

    @Mock
    private ComputerRepository computerRepository;

    @InjectMocks
    private ComputerManager computerManager;

    private Computer computer;

    @BeforeEach
    void setUp() {
        computer = new Computer(1L, "i5", 8, 500, 6000, "00-AA-BB-CC-DD");
    }

    @Test
    void addComputer_savesWhenMacAddressIsUnique() {
        when(computerRepository.existsByMacAddress("00-AA-BB-CC-DD")).thenReturn(false);
        when(computerRepository.save(computer)).thenReturn(computer);

        Computer result = computerManager.addComputer(computer);

        assertThat(result).isEqualTo(computer);
        verify(computerRepository).save(computer);
    }

    @Test
    void addComputer_throwsWhenMacAddressAlreadyExists() {
        when(computerRepository.existsByMacAddress("00-AA-BB-CC-DD")).thenReturn(true);

        assertThatThrownBy(() -> computerManager.addComputer(computer))
                .isInstanceOf(DuplicateMacAddressException.class);

        verify(computerRepository, never()).save(any());
    }

    @Test
    void getComputerById_returnsComputerWhenFound() {
        when(computerRepository.findById(1L)).thenReturn(Optional.of(computer));

        Computer result = computerManager.getComputerById(1L);

        assertThat(result).isEqualTo(computer);
    }

    @Test
    void getComputerById_throwsWhenNotFound() {
        when(computerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> computerManager.getComputerById(99L))
                .isInstanceOf(ComputerNotFoundException.class);
    }

    @Test
    void updateComputer_updatesFieldsWhenFound() {
        Computer updatePayload = new Computer(null, "i9", 32, 2000, 15000, "00-AA-BB-CC-DD");
        when(computerRepository.findById(1L)).thenReturn(Optional.of(computer));
        when(computerRepository.save(any(Computer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Computer result = computerManager.updateComputer(1L, updatePayload);

        assertThat(result.getProce()).isEqualTo("i9");
        assertThat(result.getRam()).isEqualTo(32);
        assertThat(result.getIdPc()).isEqualTo(1L);
    }

    @Test
    void updateComputer_throwsWhenNewMacAddressBelongsToAnotherComputer() {
        Computer updatePayload = new Computer(null, "i9", 32, 2000, 15000, "11-AA-BB-CC-DD");
        when(computerRepository.findById(1L)).thenReturn(Optional.of(computer));
        when(computerRepository.existsByMacAddress("11-AA-BB-CC-DD")).thenReturn(true);

        assertThatThrownBy(() -> computerManager.updateComputer(1L, updatePayload))
                .isInstanceOf(DuplicateMacAddressException.class);
    }

    @Test
    void deleteComputer_deletesWhenExists() {
        when(computerRepository.existsById(1L)).thenReturn(true);

        computerManager.deleteComputer(1L);

        verify(computerRepository).deleteById(1L);
    }

    @Test
    void deleteComputer_throwsWhenNotFound() {
        when(computerRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> computerManager.deleteComputer(99L))
                .isInstanceOf(ComputerNotFoundException.class);

        verify(computerRepository, never()).deleteById(anyLong());
    }

    @Test
    void getComputersByPrice_delegatesToRepository() {
        when(computerRepository.findByPriceLessThan(6000)).thenReturn(List.of(computer));

        List<Computer> result = computerManager.getComputersByPrice(6000);

        assertThat(result).containsExactly(computer);
    }
}
