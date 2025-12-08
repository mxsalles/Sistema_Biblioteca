package repository;

import model.Emprestimo;
import model.Usuario;
import model.Livro;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;


public class EmprestimoRepository {


    public Emprestimo salvar(Emprestimo emprestimo) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(emprestimo);
            em.getTransaction().commit();
            return emprestimo;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Erro ao salvar empréstimo: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public Emprestimo atualizar(Emprestimo emprestimo) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Emprestimo emprestimoAtualizado = em.merge(emprestimo);
            em.getTransaction().commit();
            return emprestimoAtualizado;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Erro ao atualizar empréstimo: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public void remover(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Emprestimo emprestimo = em.find(Emprestimo.class, id);
            if (emprestimo != null) {
                em.remove(emprestimo);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Erro ao remover empréstimo: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public Emprestimo buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Emprestimo.class, id);
        } finally {
            em.close();
        }
    }


    public List<Emprestimo> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Emprestimo> query = em.createQuery(
                    "SELECT e FROM Emprestimo e ORDER BY e.dataEmprestimo DESC", Emprestimo.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Emprestimo> listarAtivos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Emprestimo> query = em.createQuery(
                    "SELECT e FROM Emprestimo e WHERE e.ativo = true ORDER BY e.dataEmprestimo DESC", Emprestimo.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Emprestimo> listarAtivosPorUsuario(Usuario usuario) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Emprestimo> query = em.createQuery(
                    "SELECT e FROM Emprestimo e WHERE e.usuario = :usuario AND e.ativo = true ORDER BY e.dataEmprestimo DESC", 
                    Emprestimo.class);
            query.setParameter("usuario", usuario);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public long contarEmprestimosAtivos(Usuario usuario) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(e) FROM Emprestimo e WHERE e.usuario = :usuario AND e.ativo = true", Long.class);
            query.setParameter("usuario", usuario);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    public List<Emprestimo> listarPorLivro(Livro livro) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Emprestimo> query = em.createQuery(
                    "SELECT e FROM Emprestimo e WHERE e.livro = :livro ORDER BY e.dataEmprestimo DESC", 
                    Emprestimo.class);
            query.setParameter("livro", livro);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Emprestimo> listarAtrasados() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Emprestimo> query = em.createQuery(
                    "SELECT e FROM Emprestimo e WHERE e.ativo = true AND e.dataDevolucaoPrevista < CURRENT_DATE ORDER BY e.dataDevolucaoPrevista", 
                    Emprestimo.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Emprestimo> listarHistoricoPorUsuario(Usuario usuario) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Emprestimo> query = em.createQuery(
                    "SELECT e FROM Emprestimo e WHERE e.usuario = :usuario ORDER BY e.dataEmprestimo DESC", 
                    Emprestimo.class);
            query.setParameter("usuario", usuario);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
