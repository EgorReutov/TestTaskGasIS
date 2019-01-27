package ru.reutovegor.dao;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.reutovEgor.dao.ControlDBImpl;
import ru.reutovEgor.item.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class ControlDBImplTest {
    private ControlDBImpl store;

    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPrStatement;
    @Mock
    private ResultSet mockResultSet;

    @Before
    public void doBefore() throws SQLException {
        MockitoAnnotations.initMocks(this);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPrStatement);
        this.store = new ControlDBImpl(mockConnection);
    }

    @Test
    public void whenFindByNameThenReturnNotNullUser() throws SQLException {
        when(mockPrStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("surname")).thenReturn("Reutov");
        User expected = new User("Egor", "Reutov");
        User result = store.findUserByName("Egor");
        assertEquals(expected, result);
        verify(mockConnection).prepareStatement("SELECT surname FROM users WHERE name = ?");
        verify(mockPrStatement).setString(1, "Egor");
        verify(mockPrStatement).executeQuery();
        verify(mockResultSet).next();
        verify(mockResultSet).getString("surname");
        verify(mockResultSet).close();
        verify(mockPrStatement).close();
        verifyNoMoreInteractions(mockConnection, mockPrStatement, mockResultSet);
    }

    @Test
    public void whenFindByNameThenReturnNullUser() throws SQLException {
        when(mockPrStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);
        User result = store.findUserByName("Elena");
        assertNull(result);
        verify(mockConnection).prepareStatement("SELECT surname FROM users WHERE name = ?");
        verify(mockPrStatement).setString(1, "Elena");
        verify(mockPrStatement).executeQuery();
        verify(mockResultSet).next();
        verify(mockResultSet).close();
        verify(mockPrStatement).close();
        verifyNoMoreInteractions(mockConnection, mockPrStatement, mockResultSet);
    }

    @Test
    public void whenUpdateSurnameThenReturnNewUser() throws SQLException {
        when(mockPrStatement.executeUpdate()).thenReturn(1);
        User expected = new User("1", "Petr");
        User result = store.updateSurname(expected);
        assertEquals(expected, result);
        verify(mockConnection).prepareStatement("UPDATE users SET surname = ? WHERE name = ?");
        verify(mockPrStatement).setString(1, "Petr");
        verify(mockPrStatement).setString(2, "1");
        verify(mockPrStatement).executeUpdate();
        verify(mockPrStatement).close();
        verifyNoMoreInteractions(mockConnection, mockPrStatement);
    }

    @Test
    public void whenUpdateSurnameThenReturnNullUser() throws SQLException {
        when(mockPrStatement.executeUpdate()).thenReturn(0);
        User result = store.updateSurname(new User("Elena", "Shilikova"));
        assertNull(result);
        verify(mockConnection).prepareStatement("UPDATE users SET surname = ? WHERE name = ?");
        verify(mockPrStatement).setString(1, "Shilikova");
        verify(mockPrStatement).setString(2, "Elena");
        verify(mockPrStatement).executeUpdate();
        verify(mockPrStatement).close();
        verifyNoMoreInteractions(mockConnection, mockPrStatement);
    }

    @Test
    public void whenUpdateSurnameThenThrowSQLException() throws SQLException {
        when(mockPrStatement.executeUpdate()).thenThrow(new SQLException());
        store.updateSurname(new User("Maria", "Ivanova"));
        verify(mockConnection).prepareStatement("UPDATE users SET surname = ? WHERE name = ?");
        verify(mockPrStatement).setString(1, "Ivanova");
        verify(mockPrStatement).setString(2, "Maria");
        verify(mockPrStatement).executeUpdate();
        verify(mockPrStatement).close();
        verifyNoMoreInteractions(mockConnection, mockPrStatement);
    }
}
