package org.example;


import org.example.dao.ProfesseurDAO;
import org.example.entity.ElementModule;
import org.example.entity.Professeur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.*;
import java.util.*;


import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.*;

class ProfesseurDAOTest {

    private ProfesseurDAO professeurDAO;
    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        professeurDAO = new ProfesseurDAO();
    }

    @Test
    void testSave() throws SQLException {
        Professeur professeur = new Professeur();
        professeur.setCode("PR001");
        professeur.setNom("Doe");
        professeur.setPrenom("John");
        professeur.setSpecialite("Mathematics");

        when(mockConnection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong(1)).thenReturn(1L);

        Professeur savedProfesseur = professeurDAO.save(professeur);

        assertEquals(1L, savedProfesseur.getId().longValue());
        verify(mockPreparedStatement).setString(1, "PR001");
        verify(mockPreparedStatement).setString(2, "Doe");
        verify(mockPreparedStatement).setString(3, "John");
        verify(mockPreparedStatement).setString(4, "Mathematics");
    }

    @Test
    void testUpdate() throws SQLException {
        Professeur professeur = new Professeur();
        professeur.setId(1L);
        professeur.setCode("PR002");
        professeur.setNom("Smith");
        professeur.setPrenom("Jane");
        professeur.setSpecialite("Physics");

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        Professeur updatedProfesseur = professeurDAO.update(professeur);

        assertEquals(1, updatedProfesseur.getId().longValue());
        verify(mockPreparedStatement).setString(1, "PR002");
        verify(mockPreparedStatement).setString(2, "Smith");
        verify(mockPreparedStatement).setString(3, "Jane");
        verify(mockPreparedStatement).setString(4, "Physics");
        verify(mockPreparedStatement).setLong(6, 1L);
    }

    @Test
    void testFindById() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong("id")).thenReturn(1L);
        when(mockResultSet.getString("code")).thenReturn("PR001");
        when(mockResultSet.getString("nom")).thenReturn("Doe");
        when(mockResultSet.getString("prenom")).thenReturn("John");
        when(mockResultSet.getString("specialite")).thenReturn("Mathematics");

        Optional<Professeur> optionalProfesseur = professeurDAO.findById(1L);

        assertTrue(optionalProfesseur.isPresent());
        Professeur professeur = optionalProfesseur.get();
        assertEquals(1L, professeur.getId().longValue());
        assertEquals("PR001", professeur.getCode());
        assertEquals("Doe", professeur.getNom());
        assertEquals("John", professeur.getPrenom());
        assertEquals("Mathematics", professeur.getSpecialite());
    }

    @Test
    void testFindAll() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getLong("id")).thenReturn(1L, 2L);
        when(mockResultSet.getString("code")).thenReturn("PR001", "PR002");
        when(mockResultSet.getString("nom")).thenReturn("Doe", "Smith");
        when(mockResultSet.getString("prenom")).thenReturn("John", "Jane");
        when(mockResultSet.getString("specialite")).thenReturn("Mathematics", "Physics");

        List<Professeur> professeurs = professeurDAO.findAll();

        assertEquals(2, professeurs.size());
        assertEquals("PR001", professeurs.get(0).getCode());
        assertEquals("PR002", professeurs.get(1).getCode());
    }

    @Test
    void testDelete() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        professeurDAO.delete(1L);

        verify(mockPreparedStatement).setLong(1, 1L);
        verify(mockPreparedStatement).executeUpdate();
    }
}

