import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ClienteService } from '../../core/services/cliente.service';
import { Cliente } from '../../core/models/cliente.model';

@Component({
  selector: 'app-client-list',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './client-list.component.html',
  styleUrl: './client-list.component.css'
})
export class ClientListComponent implements OnInit {
  private clienteService = inject(ClienteService);
  private fb = inject(FormBuilder);
  
  clientes = signal<Cliente[]>([]);
  
  // Estado para los modales
  isCreateModalOpen = signal<boolean>(false);
  isEditModalOpen = signal<boolean>(false);
  isDeleteModalOpen = signal<boolean>(false);
  
  // Cliente seleccionado para editar/eliminar
  selectedCliente: Cliente | null = null;
  
  // Formulario reactivo para edición
  editForm: FormGroup;
  createForm: FormGroup;
  
  // Mensajes de feedback
  successMessage = signal<string>('');

  constructor() {
    this.editForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(3)]],
      telefono: ['', [Validators.required, Validators.pattern('^[0-9+ ]+$')]],
      direccion: ['', Validators.required],
      preferencias: [''],
      puntosFidelidad: [0, [Validators.required, Validators.min(0)]]
    });

    this.createForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      telefono: ['', [Validators.required, Validators.pattern('^[0-9+ ]+$')]],
      direccion: ['', Validators.required],
      preferencias: [''],
      puntosFidelidad: [0, [Validators.required, Validators.min(0)]]
    });
  }

  ngOnInit() {
    this.loadClientes();
  }

  loadClientes() {
    this.clienteService.getClientes().subscribe({
      next: (data) => {
        this.clientes.set(data);
      },
      error: (err) => {
        console.error('Error fetching clients:', err);
        alert('Error al conectar con el servidor.');
      }
    });
  }

  // --- Lógica de Creación ---
  openCreateModal() {
    this.createForm.reset({
      puntosFidelidad: 0
    });
    this.isCreateModalOpen.set(true);
  }

  closeCreateModal() {
    this.isCreateModalOpen.set(false);
  }

  submitCreate() {
    if (this.createForm.invalid) {
      this.createForm.markAllAsTouched();
      return;
    }

    const formValues = this.createForm.value;
    const nuevoCliente: Partial<Cliente> = {
      nombre: formValues.nombre,
      email: formValues.email,
      password: formValues.password,
      telefono: formValues.telefono,
      direccion: formValues.direccion,
      preferencias: formValues.preferencias?.trim() ? formValues.preferencias : 'Ninguna',
      puntosFidelidad: formValues.puntosFidelidad !== null ? formValues.puntosFidelidad : 0
    };

    this.clienteService.crearCliente(nuevoCliente).subscribe({
      next: () => {
        this.showSuccessMessage('Cliente creado exitosamente');
        this.closeCreateModal();
        this.loadClientes(); // Recargar lista
      },
      error: (err) => {
        console.error('Error creating client:', err);
        alert('Hubo un error al crear el cliente.');
      }
    });
  }

  // --- Lógica de Edición ---
  openEditModal(cliente: Cliente) {
    this.selectedCliente = cliente;
    this.editForm.patchValue({
      nombre: cliente.nombre,
      telefono: cliente.telefono,
      direccion: cliente.direccion,
      preferencias: cliente.preferencias,
      puntosFidelidad: cliente.puntosFidelidad
    });
    this.isEditModalOpen.set(true);
  }

  closeEditModal() {
    this.isEditModalOpen.set(false);
    this.selectedCliente = null;
    this.editForm.reset();
  }

  submitEdit() {
    if (this.editForm.invalid || !this.selectedCliente) {
      this.editForm.markAllAsTouched();
      return;
    }

    const formValues = this.editForm.value;
    const clienteActualizado: Partial<Cliente> = {
      nombre: formValues.nombre,
      telefono: formValues.telefono,
      direccion: formValues.direccion,
      preferencias: formValues.preferencias,
      puntosFidelidad: formValues.puntosFidelidad
    };

    this.clienteService.updateCliente(this.selectedCliente.id, clienteActualizado).subscribe({
      next: (updated) => {
        this.showSuccessMessage('Cambios aplicados exitosamente');
        this.closeEditModal();
        this.loadClientes(); // Recargar lista para ver los cambios
      },
      error: (err) => {
        console.error('Error updating client:', err);
        alert('Hubo un error al aplicar los cambios.');
      }
    });
  }

  // --- Lógica de Eliminación ---
  openDeleteModal(cliente: Cliente) {
    this.selectedCliente = cliente;
    this.isDeleteModalOpen.set(true);
  }

  closeDeleteModal() {
    this.isDeleteModalOpen.set(false);
    this.selectedCliente = null;
  }

  confirmDelete() {
    if (!this.selectedCliente) return;

    this.clienteService.deleteCliente(this.selectedCliente.id).subscribe({
      next: () => {
        this.showSuccessMessage('Cliente eliminado exitosamente');
        this.closeDeleteModal();
        this.loadClientes(); // Recargar lista
      },
      error: (err) => {
        console.error('Error deleting client:', err);
        alert('Hubo un error al eliminar el cliente.');
      }
    });
  }
  
  // Utilidad para mostrar el mensaje de éxito temporalmente
  private showSuccessMessage(msg: string) {
    this.successMessage.set(msg);
    setTimeout(() => {
      this.successMessage.set('');
    }, 3000);
  }
}
