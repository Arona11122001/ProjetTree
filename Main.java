import java.io.File;
import java.util.ArrayList;
import java.util.List;

abstract class ComposantSystemeFichiers {
    protected File fichier;

    public ComposantSystemeFichiers(File fichier) {
        this.fichier = fichier;
    }

    public abstract void afficher(String prefixe, boolean estDernier);
}

class FeuilleFichier extends ComposantSystemeFichiers {
    public FeuilleFichier(File fichier) {
        super(fichier);
    }

    public void afficher(String prefixe, boolean estDernier) {
        System.out.print(prefixe);
        System.out.print(estDernier ? "└── " : "├── ");
        System.out.println(fichier.getName());
    }
}

class RepertoireComposite extends ComposantSystemeFichiers {
    private List<ComposantSystemeFichiers> enfants;

    public RepertoireComposite(File repertoire) {
        super(repertoire);
        this.enfants = new ArrayList<>();
    }

    public void ajouterComposant(ComposantSystemeFichiers composant) {
        enfants.add(composant);
    }

    public void afficher(String prefixe, boolean estDernier) {
        System.out.print(prefixe);
        System.out.print(estDernier ? "└── " : "├── ");
        System.out.println(fichier.getName() + File.separator);

        for (int i = 0; i < enfants.size() - 1; i++) {
            ComposantSystemeFichiers composant = enfants.get(i);
            composant.afficher(prefixe + (estDernier ? "    " : "│   "), false);
        }

        if (!enfants.isEmpty()) {
            ComposantSystemeFichiers dernierComposant = enfants.get(enfants.size() - 1);
            dernierComposant.afficher(prefixe + (estDernier ? "    " : "│   "), true);
        }
    }
}

class ArborescenceSystemeFichiers {
    private ComposantSystemeFichiers racine;

    public ArborescenceSystemeFichiers(File repertoireRacine) {
        this.racine = creerArborescence(repertoireRacine);
    }

    private ComposantSystemeFichiers creerArborescence(File repertoire) {
        RepertoireComposite composite = new RepertoireComposite(repertoire);
        File[] fichiers = repertoire.listFiles();
        if (fichiers != null) {
            for (File fichier : fichiers) {
                if (fichier.isDirectory()) {
                    ComposantSystemeFichiers composant = creerArborescence(fichier);
                    composite.ajouterComposant(composant);
                } else {
                    ComposantSystemeFichiers composant = new FeuilleFichier(fichier);
                    composite.ajouterComposant(composant);
                }
            }
        }
        return composite;
    }

    public void afficher() {
        racine.afficher("", true);
    }
}

public class Main {
    public static void main(String[] args) {
        File repertoireRacine = new File("/home/arona/Bureau/tpPHP-Arona-Bassirou");
        ArborescenceSystemeFichiers arborescenceSystemeFichiers = new ArborescenceSystemeFichiers(repertoireRacine);
        arborescenceSystemeFichiers.afficher();
    }
}