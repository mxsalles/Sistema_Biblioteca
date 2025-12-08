package repository;

import model.Livro;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;


public class LivroRepository {

    public Livro salvar(Livro livro) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(livro);
            em.getTransaction().commit();
            return livro;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Erro ao salvar livro: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
    public Livro atualizar(Livro livro) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Livro livroAtualizado = em.merge(livro);
            em.getTransaction().commit();
            return livroAtualizado;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Erro ao atualizar livro: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public void remover(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Livro livro = em.find(Livro.class, id);
            if (livro != null) {
                em.remove(livro);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Erro ao remover livro: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public Livro buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Livro.class, id);
        } finally {
            em.close();
        }
    }

    public Livro buscarPorIsbn(String isbn) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Livro> query = em.createQuery(
                    "SELECT l FROM Livro l WHERE l.isbn = :isbn", Livro.class);
            query.setParameter("isbn", isbn);
            List<Livro> resultados = query.getResultList();
            return resultados.isEmpty() ? null : resultados.get(0);
        } finally {
            em.close();
        }
    }

    public List<Livro> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Livro> query = em.createQuery("SELECT l FROM Livro l ORDER BY l.titulo", Livro.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Livro> listarDisponiveis() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Livro> query = em.createQuery(
                    "SELECT l FROM Livro l WHERE l.quantidadeDisponivel > 0 ORDER BY l.titulo", Livro.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Livro> buscarPorTitulo(String titulo) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Livro> query = em.createQuery(
                    "SELECT l FROM Livro l WHERE LOWER(l.titulo) LIKE LOWER(:titulo) ORDER BY l.titulo", Livro.class);
            query.setParameter("titulo", "%" + titulo + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Livro> buscarPorAutor(String autor) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Livro> query = em.createQuery(
                    "SELECT l FROM Livro l WHERE LOWER(l.autor) LIKE LOWER(:autor) ORDER BY l.titulo", Livro.class);
            query.setParameter("autor", "%" + autor + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Livro> buscarPorTema(String tema) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Livro> query = em.createQuery(
                    "SELECT l FROM Livro l WHERE LOWER(l.tema) LIKE LOWER(:tema) ORDER BY l.titulo", Livro.class);
            query.setParameter("tema", "%" + tema + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
