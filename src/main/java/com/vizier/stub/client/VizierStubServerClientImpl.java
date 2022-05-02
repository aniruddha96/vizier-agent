/**
 * 
 */
package com.vizier.stub.client;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.vizier.stub.client.VizierStubServerClient;

/**
 * @author aniruddha
 *
 */
public class VizierStubServerClientImpl implements VizierStubServerClient {

	public boolean getAllStubs(String serverAddress, String extractTo) throws MalformedURLException {
		// This code needs to be modified at little after making changes to lauch stub
		// server
		// from Vizier db.
		String stubServerAddress = GetStubServerAddress();
		if (!GetStubServerAddress().isEmpty()) {
			serverAddress = stubServerAddress;
		}
		URL url = new URL(serverAddress + "getallstubs");
		try (InputStream in = url.openStream();
				ReadableByteChannel rbc = Channels.newChannel(in);
				FileOutputStream fos = new FileOutputStream("temp.tar.gz")) {
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Path ap = Paths.get("").toAbsolutePath().normalize();
		Path source = ap.resolve("temp.tar.gz").normalize();

		Path target = Paths.get(extractTo).toAbsolutePath().normalize();

		try {
			decompressTarGzipFile(source, target);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	public static void decompressTarGzipFile(Path source, Path target) throws IOException {

		if (Files.notExists(source)) {
			throw new IOException("File doesn't exists!");
		}

		try (InputStream fi = Files.newInputStream(source);
				BufferedInputStream bi = new BufferedInputStream(fi);
				GzipCompressorInputStream gzi = new GzipCompressorInputStream(bi);
				TarArchiveInputStream ti = new TarArchiveInputStream(gzi)) {

			ArchiveEntry entry;
			while ((entry = ti.getNextEntry()) != null) {

				// create a new path, zip slip validate
				Path newPath = zipSlipProtect(entry, target);

				if (entry.isDirectory()) {
					Files.createDirectories(newPath);
				} else {

					// check parent folder again
					Path parent = newPath.getParent();
					if (parent != null) {
						if (Files.notExists(parent)) {
							Files.createDirectories(parent);
						}
					}

					// copy TarArchiveInputStream to Path newPath
					Files.copy(ti, newPath, StandardCopyOption.REPLACE_EXISTING);

				}
			}
		}
	}

	private static Path zipSlipProtect(ArchiveEntry entry, Path targetDir) throws IOException {

		Path targetDirResolved = targetDir.resolve(entry.getName());

		// make sure normalized file still has targetDir as its prefix,
		// else throws exception
		Path normalizePath = targetDirResolved.normalize();

		if (!normalizePath.startsWith(targetDir)) {
			throw new IOException("Bad entry: " + entry.getName());
		}

		return normalizePath;
	}

	private static String GetStubServerAddress() {
		CloseableHttpClient client = HttpClients.createDefault();
		try {
			String url = "http://localhost:5000/vizier-db/api/v1/";
			HttpGet request = new HttpGet(url);
			CloseableHttpResponse response = client.execute(request);

			HttpEntity entity = response.getEntity();
			if (response.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(entity);
				JSONObject responseObject = new JSONObject(result);
				JSONArray linksArray = responseObject.getJSONArray("links");
				for (int i = 0; i < linksArray.length(); i++) {
					if (linksArray.getJSONObject(i).getString("rel").equals("python.stubserver")) {
						return linksArray.getJSONObject(i).getString("href");
					}
				}
			} else {
				// TODO: Handle failure response.
				System.out.println("Could not fetch cell stubserver address from Vizierdb.");
				return "";
			}
			return "";
		} catch (Exception e) {
			System.out.println("An Exception occurred while trying to fetch stubserver address from Vizierdb");
			e.printStackTrace();
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}
}
