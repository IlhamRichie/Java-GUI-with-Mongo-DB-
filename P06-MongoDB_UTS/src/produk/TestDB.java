package Produk;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.result.UpdateResult;
import static com.mongodb.client.model.Filters.eq;
import org.bson.Document;
import org.bson.types.ObjectId;
import java.util.Date;

public class TestDB {
    public static void main(String[] args) {
        try {
            MongoDatabase database = Koneksi.sambungDB();
            
            // Melihat daftar koleksi (tabel)
            System.out.println("===============================");
            System.out.println("Daftar Tabel dalam Database");
            MongoIterable<String> tables = database.listCollectionNames();
            for (String name : tables) {
                System.out.println(name);
            }
            
            // Menambahkan data
            System.out.println("===============================");
            System.out.println("Menambahkan data");
            MongoCollection<Document> col = database.getCollection("produk 1");
            Document doc = new Document();
            doc.put("nama", "Printer InkJet");
            doc.put("harga", 1204000);
            doc.put("tanggal", new Date());
            col.insertOne(doc);
            System.out.println("Data telah disimpan dalam koleksi");
            
            // Mendapatkan id dari dokumen yang baru saja diinsert
            ObjectId id = new ObjectId(doc.get("_id").toString());
            
            // Melihat/menampilkan data
            System.out.println("===============================");
            System.out.println("Data dalam koleksi produk");
            MongoCursor<Document> cursor = col.find().iterator();
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }
            
            // Mencari dokumen berdasarkan id
            Document myDoc = col.find(eq("_id", id)).first();
            System.out.println("===============================");
            System.out.println("Pencarian data berdasarkan id " + id);
            System.out.println(myDoc.toJson());
            
            // Mengedit data
            Document docs = new Document();
            docs.put("nama", "Canon");
            Document doc_edit = new Document("$set", docs);
            UpdateResult hasil_edit = col.updateOne(eq("_id", id), doc_edit);
            System.out.println(hasil_edit.getModifiedCount());
            
            // Melihat/menampilkan data setelah pengeditan
            System.out.println("======================");
            System.out.println("Data dalam koleksi produk setelah pengeditan");
            cursor = col.find().iterator();
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }
            
            // Menghapus data
            col.deleteOne(eq("_id", id));
            
            // Melihat/menampilkan data setelah penghapusan
            System.out.println("========================");
            System.out.println("Data dalam koleksi produk setelah penghapusan");
            cursor = col.find().iterator();
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
